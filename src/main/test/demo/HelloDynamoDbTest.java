package demo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import me.madhead.aws_junit5.dynamo.v2.DynamoDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.dynamodb.DynaliteContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.regions.Region;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(DynamoDB.class)
@Testcontainers
public class HelloDynamoDbTest {

    @Container
    public DynaliteContainer dynamoDB = new DynaliteContainer();

    private AmazonDynamoDB client;

    @BeforeEach
    public void setup() {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
            "http://" + this.dynamoDB.getHost() + ":" + this.dynamoDB.getFirstMappedPort(), Region.US_EAST_1.id());
        this.client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(endpointConfiguration).build();
    }

    @Test
    public void test() throws InterruptedException {
        HelloDynamoDb hello = new HelloDynamoDb(this.client);
        hello.createTable();
        hello.insertItem(User.build(111, "aaa", "aaa@test.com", 100));
        hello.insertItem(User.build(222, "bbb", "bbb@test.com", 200));
        List<User> users = hello.getItems();
        System.out.println("### users: " + users);
        assertThat(users).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(asList(
            User.build(111, "aaa", "aaa@test.com", 100),
            User.build(222, "bbb", "bbb@test.com", 200)
        ));
    }

}