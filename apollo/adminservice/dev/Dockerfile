# Dockerfile for apollo-adminservice
# 1. Copy apollo-adminservice-${VERSION}-github.zip to current directory
# 2. Build with: docker build -t apollo-adminservice .
# 3. Run with: docker run -p 8090:8090 -e DS_URL="jdbc:mysql://fill-in-the-correct-server:3306/ApolloConfigDB?characterEncoding=utf8" -e DS_USERNAME=FillInCorrectUser -e DS_PASSWORD=FillInCorrectPassword -d -v /tmp/logs:/opt/logs --name apollo-adminservice apollo-adminservice

FROM openjdk:8-jre-alpine

ENV VERSION 1.5.1
ENV SERVER_PORT 8090
# DataSource Info
ENV DS_URL ""
ENV DS_USERNAME ""
ENV DS_PASSWORD ""
ENV eureka.client.serviceUrl.defaultZone "http://apollo-configservice-dev:8080/eureka/"

RUN echo "http://mirrors.aliyun.com/alpine/v3.8/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.8/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

ADD apollo-adminservice-${VERSION}-github.zip /apollo-adminservice/apollo-adminservice-${VERSION}-github.zip

RUN unzip /apollo-adminservice/apollo-adminservice-${VERSION}-github.zip -d /apollo-adminservice \
    && rm -rf /apollo-adminservice/apollo-adminservice-${VERSION}-github.zip

COPY ./dev/config/application-github.properties /apollo-adminservice/config/application-github.properties

RUN sed -i '$d' /apollo-adminservice/scripts/startup.sh \
    && chmod +x /apollo-adminservice/scripts/startup.sh \
    && echo "tail -f /opt/logs/100003172/apollo-adminservice.log" >> /apollo-adminservice/scripts/startup.sh

EXPOSE $SERVER_PORT

CMD ["/apollo-adminservice/scripts/startup.sh"]
