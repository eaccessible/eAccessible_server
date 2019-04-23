package Facade;
import bean.Local;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import bean.Accessibilitat;

@WebService
public class ServeiWeb {

	@WebMethod
	public void AltaLocal(Local local, List<Accessibilitat> accessibilitat) {
				
		System.out.print("\n AltaLocal");
		
		String strEstat = new String();
		Connection connection = null;
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) strEstat = "Error al crear el datasource";
				else{
					connection = ds.getConnection();
					Statement stm = connection.createStatement();
					stm.executeUpdate("insert into Local (codilocal,coditipolocal,codicarrer,nomcarrer,nomvia,numero,nomlocal,observacions,verificat) values('"+local.getCodilocal()+"','"+local.getCoditipolocal()+"','"+local.getCodicarrer()+"','"+local.getNomcarrer()+"','"+local.getNomvia()+"','"+local.getNumero()+"','"+local.getNomlocal()+"','"+local.getObservacions()+"','"+local.getVerificat()+"')");
					for(int i=0; i<accessibilitat.size(); i=i+1) {
						stm.executeUpdate("insert into Accessibilitat (codiaccessibilitat,codilocal,codicaracteristica,valor,verificat) values('"+accessibilitat.get(i).getCodiaccessibilitat()+"','"+accessibilitat.get(i).getCodilocal()+"','"+accessibilitat.get(i).getCodicaracteristica()+"','"+accessibilitat.get(i).getValor()+"','"+accessibilitat.get(i).getVerificat()+"')");
					}
					
					connection.close();
					stm.close();
				}
			}
		}
		catch(Exception e) {e.printStackTrace();}
		finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	
	
	
	@WebMethod
	public void ValidaLocal(int codiLocal) {
		
	}
	
	@WebMethod
	public void BaixaLocal(int codiLocal) {
		
	}
	
}
