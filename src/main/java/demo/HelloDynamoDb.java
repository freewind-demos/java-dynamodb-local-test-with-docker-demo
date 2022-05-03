package demo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class HelloDynamoDb {

    private AmazonDynamoDB dynamoDbClient;

    public HelloDynamoDb(AmazonDynamoDB dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public void createTable() throws InterruptedException {
        CreateTableResult result = dynamoDbClient.createTable(
            // attributes definition
            asList(
                // TL;DR Don't include any non-key attribute definitions in AttributeDefinitions.
                // https://stackoverflow.com/a/30924384/342235
//                new AttributeDefinition("email", ScalarAttributeType.S),
//                new AttributeDefinition("age", ScalarAttributeType.N),
                new AttributeDefinition("username", ScalarAttributeType.S),
                new AttributeDefinition("id", ScalarAttributeType.N)
            ),
            "users",
            // key definition
            asList(
                new KeySchemaElement("username", KeyType.HASH),
                new KeySchemaElement("id", KeyType.RANGE)
            ),
            // read-write capacity config
            new ProvisionedThroughput(10L, 10L)
        );
        System.out.println("### result: " + result);
        TableUtils.waitUntilActive(dynamoDbClient, "users");
    }

    public void insertItem(User user) {
        DynamoDB db = new DynamoDB(this.dynamoDbClient);
        Item item = new Item().withPrimaryKey("username", user.username, "id", user.id)
            .withString("email", user.email)
            .withNumber("age", user.age);

        PutItemOutcome outcome = db.getTable("users").putItem(item);
        System.out.println("insert result: " + outcome);
    }

    public List<User> getItems() {
        ScanRequest scanRequest = new ScanRequest()
            .withTableName("users")
            .withConsistentRead(false)
            .withLimit(100)
            .withAttributesToGet("id", "username", "email", "age");

        ScanResult result = this.dynamoDbClient.scan(scanRequest);
        List<Map<String, AttributeValue>> items = result.getItems();
        System.out.println("### items: " + items);
        List<User> users = new ArrayList<>();
        for (Map<String, AttributeValue> itemMap : items) {
            User user = new User();
            user.id = Integer.parseInt(itemMap.get("id").getN());
            user.username = itemMap.get("username").getS();
            user.email = itemMap.get("email").getS();
            user.age = Integer.parseInt(itemMap.get("age").getN());
            users.add(user);
        }
        return users;
    }


}
