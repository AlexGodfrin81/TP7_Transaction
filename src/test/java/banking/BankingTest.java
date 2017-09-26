package banking;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;


public class BankingTest {
	private static DataSource myDataSource; // La source de données à utiliser
	private static Connection myConnection ;	
	private BankingDAO myDAO;
	
	@Before
	public void setUp() throws SQLException, IOException, SqlToolError {
		// On crée la connection vers la base de test "in memory"
		myDataSource = getDataSource();
		myConnection = myDataSource.getConnection();
		// On initialise la base avec le contenu d'un fichier de test
		String sqlFilePath = BankingTest.class.getResource("testdata.sql").getFile();
		SqlFile sqlFile = new SqlFile(new File(sqlFilePath));
		sqlFile.setConnection(myConnection);
		sqlFile.execute();
		sqlFile.closeReader();	
		// On crée l'objet à tester
		myDAO = new BankingDAO(myDataSource);
	}
	
	@After
	public void tearDown() throws SQLException {
		myConnection.close();		
		myDAO = null; // Pas vraiment utile
	}

	
	@Test
	public void findExistingCustomer() throws SQLException {
		float balance = myDAO.balanceForCustomer(0);
		// attention à la comparaison des nombres à virgule flottante !
		assertEquals("Balance incorrecte !", 100.0f, balance, 0.001f);
	}

	@Test
	public void successfulTransfer() throws Exception {
		float before0 = myDAO.balanceForCustomer(0);
		float before1 = myDAO.balanceForCustomer(1);
		myDAO.bankTransferTransaction(0, 1, 10f);
		// Les balances doivent avoir été mises à jour dans les 2 comptes
		assertEquals("Balance incorrecte !", before0 - 10.0f, myDAO.balanceForCustomer(0), 0.001f);
		assertEquals("Balance incorrecte !", before1 + 10.0f, myDAO.balanceForCustomer(1), 0.001f);		
	}
	

	public static DataSource getDataSource() throws SQLException {
		org.hsqldb.jdbc.JDBCDataSource ds = new org.hsqldb.jdbc.JDBCDataSource();
		ds.setDatabase("jdbc:hsqldb:mem:testcase;shutdown=true");
		ds.setUser("sa");
		ds.setPassword("sa");
		return ds;
	}	
}
