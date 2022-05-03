Java DynamoDB Local Test Demo
=====================

1. 需要在本地启动一个dynamodb，为了简单起见，使用docker: testcontainers + dynalite
2. dynalite向host暴露的port是随机的（特意设计的），所以我们在构建url时，需要手动拿到该port
3. junit4与junit5的集成方法区别很大，需要使用`org.testcontainers:junit-jupiter`，以及`@Testcontainers/@Container`，其内容只占了网站中的一页: https://www.testcontainers.org/test_framework_integration/junit_5/
4. 网上有很多相关的内容，但都存在各种问题，不好用（见pom.xml中被注释掉的库）
5. 测试运行很慢，启动docker及建表需要20多秒，所以做不到每个测试前新开一个干净环境，只能一次性建好表，然后每个测试注意环境中其它数据对自己的影响

总体来说不太理想，如果有一个内存中的mock就好了，估计不太好弄。

好像还有一种直接调用jar+sql4j的方式，到时候试试，希望能快一点。

Run `HelloDynamoDbTest.java` in your IDE.