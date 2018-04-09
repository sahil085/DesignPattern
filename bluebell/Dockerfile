FROM ttnd/tomcat:8-jdk8

RUN update-java-alternatives -s java-1.8.0-openjdk-amd64

RUN shutdown.sh \
    && rm -rf $CATALINA_HOME/webapps/*

COPY . /code
WORKDIR /code

# get maven 3.3.9
RUN wget --no-verbose -O /tmp/apache-maven-3.3.9.tar.gz http://archive.apache.org/dist/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz

# verify checksum
RUN echo "516923b3955b6035ba6b0a5b031fbd8b /tmp/apache-maven-3.3.9.tar.gz" | md5sum -c

# install maven
RUN tar xzf /tmp/apache-maven-3.3.9.tar.gz -C /opt/
RUN ln -s /opt/apache-maven-3.3.9 /opt/maven
RUN ln -s /opt/maven/bin/mvn /usr/local/bin
RUN rm -f /tmp/apache-maven-3.3.9.tar.gz
ENV MAVEN_HOME /opt/maven

# remove download archive files
RUN apt-get clean

RUN mvn clean install

RUN cp bluebell-360/target/bluebell-360.war $CATALINA_HOME/webapps/bluebell.war

RUN cp -R bluebell-client/ $CATALINA_HOME/webapps/

# RUN cp -R new-bluebell-client/dist $CATALINA_HOME/webapps/new-bluebell-client
# Remove code base from image
RUN rm -rf *

EXPOSE 8080

CMD ["catalina.sh", "run"]
