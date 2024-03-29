# Dockerfile for apollo-configservice
# 1. Copy apollo-configservice-${VERSION}-github.zip to current directory
# 2. Build with: docker build -t apollo-configservice .
# 3. Run with: docker run -p 8080:8080 -e DS_URL="jdbc:mysql://fill-in-the-correct-server:3306/ApolloConfigDB?characterEncoding=utf8" -e DS_USERNAME=FillInCorrectUser -e DS_PASSWORD=FillInCorrectPassword -d -v /tmp/logs:/opt/logs --name apollo-configservice apollo-configservice

#FROM java:8
FROM openjdk:8-jre-alpine

ENV VERSION 1.5.1
ENV SERVER_PORT 8080
# DataSource Info
ENV DS_URL ""
ENV DS_USERNAME ""
ENV DS_PASSWORD ""


RUN echo "http://mirrors.aliyun.com/alpine/v3.8/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.8/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

ADD ./apollo-configservice-${VERSION}-github.zip /apollo-configservice/apollo-configservice-${VERSION}-github.zip

RUN unzip /apollo-configservice/apollo-configservice-${VERSION}-github.zip -d /apollo-configservice \
    && rm -rf /apollo-configservice/apollo-configservice-${VERSION}-github.zip

COPY ./dev/config/application-github.properties /apollo-configservice/config/application-github.properties

RUN sed -i '$d' /apollo-configservice/scripts/startup.sh \
    && chmod +x /apollo-configservice/scripts/startup.sh \
    && echo "tail -f /opt/logs/100003171/apollo-configservice.log" >> /apollo-configservice/scripts/startup.sh

EXPOSE $SERVER_PORT

CMD ["/apollo-configservice/scripts/startup.sh"]
