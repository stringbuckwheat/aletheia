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
