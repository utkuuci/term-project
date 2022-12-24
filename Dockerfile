FROM openjdk:18
EXPOSE 80
WORKDIR /app
COPY . .
CMD [ "java", "-classpath", "./bin", "Project.Proxy.ProxyServer" ]
