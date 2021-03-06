package ch.baloise.keycloak.client.admin;

import org.junit.*;

/**
 * Provides several tests to ensure that we have proper functionality in our facade.
 */
@Ignore("Just for local testing")
public class KeycloakAdminTest {

    private static final String SERVER_URL = "http://localhost:8080/auth";
    private static final String REALM_ID = "workshop";
    private static final String CLIENT_ID = "keycloak-admin";
    private static final String CLIENT_SECRET = "ab14c208-a2ee-47a9-9fd5-04c93cc61a2a";

    /**
     * Provides a facade instance we create once before executing all tests.
     */
    private static KeycloakAdminFacade _facade;

    @BeforeClass
    public static void initFacade() {
        _facade = new KeycloakAdminFacadeImpl();
        _facade.connect(SERVER_URL, REALM_ID, CLIENT_ID, CLIENT_SECRET);
    }

    @AfterClass
    public static void tearDownFacade() {
        //close the facade
        _facade.close();
    }

    @Test
    public void givenEmailFragment_whenSearch_thenExpectedResult() {
        //positive
        Assert.assertEquals(1, _facade.findUsersByMail("bruce.wayne@example.com", 0, 10).size());
        Assert.assertEquals(4, _facade.findUsersByMail("@example.com", 0, 10).size());

        //negative
        Assert.assertEquals(0, _facade.findUsersByMail("@google.com", 0, 10).size());

        //null
        Assert.assertEquals(6, _facade.findUsersByMail(null, 0, 10).size());
        Assert.assertEquals(6, _facade.findUsersByMail("", 0, 10).size());
    }

    @Test
    public void givenRoleName_whenSearch_thenExpectedResult() {
        //positive
        Assert.assertEquals(2, _facade.findUsersByRole("library_user").size());
        Assert.assertEquals(1, _facade.findUsersByRole("library_admin").size());
        Assert.assertEquals(3, _facade.findUsersByRole("library_curator").size());

        //negative
        Assert.assertEquals(0, _facade.findUsersByRole("mumu").size());

        //null
        Assert.assertEquals(0, _facade.findUsersByRole(null).size());
        Assert.assertEquals(0, _facade.findUsersByRole("").size());
    }

    @Test
    public void givenId_whenSearch_thenExpectedResult() {
        //positive
        Assert.assertNotNull(_facade.getUserById("bwayne"));

        //negative
        Assert.assertFalse(_facade.getUserById("mumu").isPresent());

        //null
        Assert.assertFalse(_facade.getUserById(null).isPresent());
        Assert.assertFalse(_facade.getUserById("").isPresent());
    }
}
