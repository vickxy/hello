FROM centos:centos7

WORKDIR /usr/lib/grpc

COPY jre-8u141-linux-x64.rpm jre-8u141-linux-x64.rpm
RUN yum localinstall jre-8u141-linux-x64.rpm -y
RUN ["/usr/bin/java", "-version"]
# put jar in the current directory
COPY hello-0.0.1.jar /usr/lib/grpc/hello-0.0.1.jar

ENTRYPOINT ping localhost
