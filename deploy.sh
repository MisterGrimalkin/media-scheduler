mvn clean package
sshpass -p raspberry scp -r target/ pi@192.168.0.70:scheduler
