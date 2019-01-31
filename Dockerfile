From openjdk:8
copy ./build/libs/suchmaschinen-polizei-berichte-0.0.1-SNAPSHOT.jar suchmaschinen-1.0.jar
CMD ["java","-jar","suchmaschinen-1.0.jar"]