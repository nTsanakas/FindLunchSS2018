FROM mariadb:latest
ENV MYSQL_ROOT_PASSWORD=root
ENV MYSQL_USER=db_findlunch_service
ENV MYSQL_PASSWORD=LT>r71VGWS
RUN apt-get update
RUN apt-get -y upgrade
RUN apt-get dist-upgrade -y
RUN apt-get install -y default-jre
RUN apt-get install -y supervisor
RUN echo 'deb http://http.debian.net/debian jessie-backports main' >> /etc/apt/sources.list
RUN apt-get update
RUN apt-get install -t jessie-backports -y ca-certificates-java
RUN apt-get install -t jessie-backports -y openjdk-8-jre-headless
RUN update-java-alternatives --set java-1.8.0-openjdk-amd64
EXPOSE 8080
VOLUME /tmp
ARG SCHEMA
ADD ${SCHEMA} /docker-entrypoint-initdb.d/00-schema.sql
ARG DATA
ADD ${DATA} /docker-entrypoint-initdb.d/01-data.sql
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ADD supervisord.conf /etc/supervisor/conf.d/supervisord.conf
ENTRYPOINT ["/usr/bin/supervisord"]