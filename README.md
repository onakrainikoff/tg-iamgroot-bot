## I am Groot Telegram Bot
A Telegram-bot that always replies "I am Groot"

### Latest version
```1.0```

### Demo
Bot link [t.me/IamBabyGrootBot](https://t.me/IamBabyGrootBot)

#### Private chat
![private chat](doc/img/private_chat.jpg)
#### Group chat
![group chat](doc/img/group_chat.jpg)

### Releases
[Releases list](https://github.com/onakrainikoff/tg-iamgroot-bot/releases)

[Docker Images list](https://github.com/onakrainikoff/tg-iamgroot-bot/pkgs/container/tg-iamgroot-bot)

[Jars list](https://github.com/onakrainikoff/tg-iamgroot-bot/packages/1295224/versions)

### Run
#### Install helm chart
```
helm repo add onakrainikoff https://onakrainikoff.github.io/helm-charts
helm repo update
helm install iamgroot-bot onakrainikoff/tg-iamgroot-bot \
--set bot.user_name=<name> \
--set bot.token=<token> \
--set bot.threads_count=5
```

#### Run in docker
```
docker run --rm -d \
--name iamgroot-bot \
-p 8080:8080 \
-e BOT_USER_NAME=<name> \
-e BOT_TOKEN=<token> \
-e BOT_THREADS_COUNT=5 \
ghcr.io/onakrainikoff/tg-iamgroot-bot:<version>
```

#### Run in jar
```
BOT_USER_NAME=<name> BOT_TOKEN=<token> BOT_THREADS_COUNT=5 java -jar iamgroot-bot-<version>.jar
```

### Build from Sources
#### Build jar
```
mvn clean install -Dversion=<version>
```
#### Build docker image

```
mvn clean install -Dversion=<version> -P docker
or for tcp
mvn clean install -Dversion=<version> -Ddocker_host=<ip>
```
