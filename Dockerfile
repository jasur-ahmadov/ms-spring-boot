FROM openjdk
RUN echo "hello world"
COPY ./build/libs/ms-spring-boot-1.0.0.jar /app/
WORKDIR /app/
ENTRYPOINT ["java"]
CMD ["-jar", "/app/ms-spring-boot-1.0.0.jar"]