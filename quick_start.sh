# yml 이름 수정
# 파일이 존재하는지 확인

# 현재 작업 디렉토리를 스크립트가 위치한 곳으로 설정
cd "$(dirname "$0")"

# 파일 이름 변경 함수
rename_file() {
    local dir=$1
    local old_name=$2

    if [ -f "$dir/$old_name" ]; then
        mv "$dir/$old_name" "$dir/application.yml"
        echo "$old_name을 application.yml로 변경 성공"
    else
        echo "$dir에 $old_name이 없습니다."
    fi
}

# 파일 이름 변경
rename_file "auth/src/main/resources" "application-auth.yml"
rename_file "resource/src/main/resources" "application-resource.yml"


# 현재 실행 중인 서버 PID 찾기 및 종료
echo ">>>>> 해당 포트에 실행중인 서버 종료"
for port in 8888 9999 50051 50052; do
    PIDs=$(lsof -t -i:$port 2>/dev/null)
    if [ -n "$PIDs" ]; then
        for PID in $PIDs; do
            echo ">>> 포트 $port (PID: $PID) 프로세스 종료..."
            kill "$PID"
        done
    else
        echo "포트 $port 프로세스 없음"
    fi
done

# 빌드 디렉토리 삭제
echo ">>>>> 빌드 디렉토리 삭제..."
rm -rf auth/build
rm -rf resource/build

# gRPC 프로토콜 파일 생성
echo ">>>>> gRPC 프로토콜 파일 생성..."
(cd auth && ./gradlew generateProto)
(cd resource && ./gradlew generateProto)

# 전체 빌드
echo ">>>>> 인증 서버 빌드..."
(cd auth && ./gradlew build -x test)

echo ">>>>> 자원 서버 빌드..."
(cd resource && ./gradlew build -x test)


# 서버 실행 (백그라운드에서 실행)
echo ">>>>> 백그라운드 서버 실행: 인증 서버..."
nohup bash -c "(cd auth && ./gradlew bootRun)" > auth.log 2>&1 &
AUTH_PID=$!

echo ">>>>> 백그라운드 서버 실행: 자원 서버..."
nohup bash -c "(cd resource && ./gradlew bootRun)" > resource.log 2>&1 &
RESOURCE_PID=$!

sleep 3

echo ">>>>> 서버 실행 완료!"
echo "자원 서버 실행 (PID: $RESOURCE_PID)"
echo "인증 서버 실행 (PID: $AUTH_PID)"
