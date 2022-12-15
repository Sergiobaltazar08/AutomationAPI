package restAssuredTokenCRUD;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CRUDItemTest {

    /*
    *   Get: token with authentication
    *   Put: Item by ID
    *   get: Item by ID
    *   put: Update Item by ID
    *   delete: Item by ID
    * */
    @Test
    public void CRUDItem(){
        JSONObject body = new JSONObject();

        System.out.println("**** GET TOKEN ****");

        Response response=given()
                .auth()
                .preemptive()
                .basic("sergiotestAPI@test.com","12345678")
                .body(body.toString())
                .log().all()
                .when()
                .get("https://todo.ly/api/authentication/token.json");

        response.then()
                .log().all()
                .statusCode(200)
                .body("UserEmail",equalTo("sergiotestAPI@test.com"));

        String token = response.then().extract().path("TokenString");





        System.out.println("**** CREATE ITEM ****");
        body.put("Content","Item-Sergio");
        response=given().header("Token",token)
                .body(body.toString())
                .log().all()
        .when()
                .post("https://todo.ly/api/items.json");

        response.then()
                .log().all()
                .statusCode(200)
                .body("Content",equalTo("Item-Sergio"));

        int idItem = response.then().extract().path("Id");



        System.out.println("**** GET ITEM BY ID ****");

        response=given().header("Token",token)
                .body(body.toString())
                .log().all()
                .when()
                .get("https://todo.ly/api/items/"+idItem+".json");

        response.then()
                .log().all()
                .statusCode(200)
                .body("Id",equalTo(idItem))
                .body("Checked",equalTo(false))
                .body("Content",equalTo("Item-Sergio"));



        System.out.println("**** UPDATE ITEM BY ID ****");

        body.put("Content","Item-Sergio-Update");
        body.put("Checked",false);
        response=given().header("Token",token)
                .body(body.toString())
                .log().all()
                .when()
                .put("https://todo.ly/api/items/"+idItem+".json");

        response.then()
                .log().all()
                .statusCode(200)
                .body("Deleted",equalTo(false))
                .body("Checked",equalTo(false))
                .body("Content",equalTo("Item-Sergio-Update"));



        
        System.out.println("**** DELETE ITEM ****");

        response=given().header("Token",token)
                .body(body.toString())
                .log().all()
                .when()
                .delete("https://todo.ly/api/items/"+idItem+".json");

        response.then()
                    .log().all()
                    .statusCode(200)
                    .body("Id",equalTo(idItem))
                    .body("Deleted",equalTo(true))
                    .body("Content",equalTo("Item-Sergio-Update"));

    }

}
