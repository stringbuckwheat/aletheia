ports=(8888 9999 50051 50052)

echo ">>>>> 서버 종료..."
for port in "${ports[@]}"; do

    processes=$(lsof -i:$port 2>/dev/null | awk 'NR>1 {print $1, $2}' | grep -v 'Postman')

    if [ -n "$processes" ]; then
        while read -r command pid; do
            echo ">>> 포트 $port (PID: $pid, COMMAND: $command) 프로세스 종료 시도..."
            # 프로세스를 종료합니다.
            kill "$pid"
            sleep 3  # 종료 대기 시간
            if ps -p "$pid" > /dev/null; then
                echo ">>> 프로세스($pid)가 종료되지 않았습니다. 강제 종료 시도..."
                kill -9 "$pid"  # 강제 종료
            else
                echo ">>> 프로세스($pid)가 정상적으로 종료되었습니다."
            fi
        done <<< "$processes"
    else
        echo "포트 $port 에 연결된 프로세스가 없습니다."
    fi
done

echo ">>>>> 서버 종료 완료!"
