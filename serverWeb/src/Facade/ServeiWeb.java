package Facade;
import bean.Local;
import bean.TipoLocal;
import exceptions.ExceptionController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import bean.Accessibilitat;


@WebService
public class ServeiWeb{
	@WebMethod
	public void AltaLocal(Local local, List<Accessibilitat> accessibilitat) throws ExceptionController {
		
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		ex.setErrorCode(101);
		if(local.getCodicarrer() <= 0) {
			ex.setErrorMessage("No s'ha introduit un codi carrer vàlid");
			throw(ex);
		}if(local.getCodilocal() <= 0) {
			ex.setErrorMessage("No s'ha introduit un codi local vàlid");
			throw(ex);
		}if(local.getCoditipolocal() <= 0) {
			ex.setErrorMessage("No s'ha introduit un codi tipo local vàlid");
			throw(ex);
		}if(local.getNomcarrer().isEmpty()) {
			ex.setErrorMessage("No s'ha introduit un nom de carrer vàlid");
			throw(ex);
		}if(local.getNomlocal().isEmpty()) {
			ex.setErrorMessage("No s'ha introduit un nom de local vàlid");
			throw(ex);
		}if(local.getNomvia().isEmpty()) {
			ex.setErrorMessage("No s'ha introduit un nom de vía vàlid");
			throw(ex);
		}if(local.getNumero() <= 0) {
			ex.setErrorMessage("No s'ha introduit un número vàlid");
			throw(ex);
		}
		
		ex.setErrorCode(102);
		for(int i=0; i<accessibilitat.size(); i=i+1) {
			if(accessibilitat.get(i).getCodiaccessibilitat() <= 0) {
				ex.setErrorMessage("No s'ha introduit un codí d'accessibilitat vàlid");
				throw(ex);
			}
			if(accessibilitat.get(i).getCodicaracteristica() <= 0) {
				ex.setErrorMessage("No s'ha introduit un codí de característica vàlid");
				throw(ex);
			}if(accessibilitat.get(i).getCodilocal() <= 0) {
				ex.setErrorMessage("No s'ha introduit un codí de local vàlid");
				throw(ex);
			}
			if(accessibilitat.get(i).getValor() <= 0) {
				ex.setErrorMessage("No s'ha introduit un valor vàlid");
				throw(ex);
			}
		}
		
		
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(103);
					ex.setErrorMessage("No hi ha connexió amb la base de dades");
					throw(ex);
				}
				else{
					connection = ds.getConnection();
					
					String query = "insert into eAccessible.local (codilocal,coditipolocal,codicarrer,nomcarrer,nomvia,numero,nomlocal,observacions,verificat) values('"+local.getCodilocal()+"','"+local.getCoditipolocal()+"','"+local.getCodicarrer()+"','"+local.getNomcarrer()+"','"+local.getNomvia()+"','"+local.getNumero()+"','"+local.getNomlocal()+"','"+local.getObservacions()+"','"+local.getVerificat()+"')";
					Statement stm = connection.createStatement();
					stm.executeUpdate(query);
					
					for(int i=0; i<accessibilitat.size(); i=i+1) {
						stm.executeUpdate("insert into Accessibilitat (codiaccessibilitat,codilocal,codicaracteristica,valor,verificat) values('"+accessibilitat.get(i).getCodiaccessibilitat()+"','"+accessibilitat.get(i).getCodilocal()+"','"+accessibilitat.get(i).getCodicaracteristica()+"','"+accessibilitat.get(i).getValor()+"','"+accessibilitat.get(i).getVerificat()+"')");
					}
					
					connection.close();
					stm.close();
				}
			}
		}
		catch(Exception e) {
			
			e.printStackTrace();
			ex.setErrorCode(104);
			ex.setErrorMessage("S'ha produït un error a la base de dades");
			
			
		}
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
	public void ValidaLocal (int codiLocal) {
		String strEstat = new String();
		Connection connection = null;
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) strEstat = "Error al crear el datasource";
				else{
					connection = ds.getConnection();
					
					String query = "update eAccessible.local set verificat='S' where codilocal="+codiLocal;
					Statement stm = connection.createStatement();
					stm.executeUpdate(query);
					
					
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
	public void BaixaLocal (int codiLocal) {
		String strEstat = new String();
		Connection connection = null;
		
		try{
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) strEstat = "Error al crear el datasource";
				else{
					connection = ds.getConnection();
					String query = "delete from eAccessible.local where codilocal="+codiLocal;
					Statement stm = connection.createStatement();
					stm.executeUpdate(query);
					
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
	public Local InfoLocal(int codiLocal) {
		String strEstat = new String();
		Connection connection = null;
		
		Local local = new Local();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) strEstat = "Error al crear el datasource";
				else{
					connection = ds.getConnection();
					
					String query = "select coditipolocal,codicarrer,nomcarrer,nomvia,codilocal,nomlocal,numero,observacions,verificat from eAccessible.local where codilocal="+codiLocal;
					Statement stm = connection.createStatement();
					ResultSet rs = stm.executeQuery(query);
					while(rs.next()) {
						local.setCoditipolocal(rs.getInt("coditipolocal"));
						local.setCodicarrer(rs.getInt("codicarrer"));
						local.setNomcarrer(rs.getString("nomcarrer"));
						local.setNomvia(rs.getString("nomvia"));
						local.setCodilocal(rs.getInt("codilocal"));
						local.setNomlocal(rs.getString("nomlocal"));
						local.setNumero(rs.getInt("numero"));
						local.setObservacions(rs.getString("observacions"));
						local.setVerificat(rs.getString("verificat"));
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
		return local;
	}
	
	@WebMethod
	public List<TipoLocal> CercaTipoLocal() {
				
		String strEstat = new String();
		Connection connection = null;
		
		List<TipoLocal> tipoLocalList = new ArrayList<TipoLocal>();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) strEstat = "Error al crear el datasource";
				else{
					connection = ds.getConnection();
					
					String query = "select coditipolocal, nomtipolocalca, nomtipolocales, nomtipolocalen from eAccessible.tipolocal";
					Statement stm = connection.createStatement();
					ResultSet rs = stm.executeQuery(query);
					while(rs.next()) {
						TipoLocal tipoLocal = new TipoLocal();
						tipoLocal.setCoditipolocal(rs.getInt("coditipolocal"));
						tipoLocal.setNomtipolocalca(rs.getString("nomtipolocalca"));
						tipoLocal.setNomtipolocales(rs.getString("nomtipolocales"));
						tipoLocal.setNomtipolocalen(rs.getString("nomtipolocalen"));
						
						tipoLocalList.add(tipoLocal);
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
		return tipoLocalList;
	}
	
	@WebMethod
	public List<Local> LocalnoVerificat() {
		String strEstat = new String();
		Connection connection = null;

		List <Local> localList = new ArrayList <Local>();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) strEstat = "Error al crear el datasource";
				else{
					connection = ds.getConnection();
					//IS NULL --> 'N'
					String query = "select coditipolocal,codicarrer,nomcarrer,nomvia,codilocal,nomlocal,numero,observacions,verificat from eAccessible.local where verificat='N'";
					Statement stm = connection.createStatement();
					ResultSet rs = stm.executeQuery(query);
					while(rs.next()) {
						Local local = new Local();
						local.setCoditipolocal(rs.getInt("coditipolocal"));
						local.setCodicarrer(rs.getInt("codicarrer"));
						local.setNomcarrer(rs.getString("nomcarrer"));
						local.setNomvia(rs.getString("nomvia"));
						local.setCodilocal(rs.getInt("codilocal"));
						local.setNomlocal(rs.getString("nomlocal"));
						local.setNumero(rs.getInt("numero"));
						local.setObservacions(rs.getString("observacions"));
						local.setVerificat(rs.getString("verificat"));
						localList.add(local);
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
		return localList;
	}
	
	@WebMethod
	public List<Local> LocalsAccessibles(int codiCaracteristica) {
		String strEstat = new String();
		Connection connection = null;
		
		List <Local> localList = new ArrayList <Local>();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) strEstat = "Error al crear el datasource";
				else{
					connection = ds.getConnection();
					
					String query = "select local.* from eAccessible.local, eAccessible.accessibilitat where accessibilitat.codicaracteristica="+codiCaracteristica+" and accessibilitat.codilocal = local.codilocal";
					Statement stm = connection.createStatement();
					ResultSet rs = stm.executeQuery(query);
					while(rs.next()) {
						Local local = new Local();
						local.setCoditipolocal(rs.getInt("coditipolocal"));
						local.setCodicarrer(rs.getInt("codicarrer"));
						local.setNomcarrer(rs.getString("nomcarrer"));
						local.setNomvia(rs.getString("nomvia"));
						local.setCodilocal(rs.getInt("codilocal"));
						local.setNomlocal(rs.getString("nomlocal"));
						local.setNumero(rs.getInt("numero"));
						local.setObservacions(rs.getString("observacions"));
						local.setVerificat(rs.getString("verificat"));
						localList.add(local);
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
		return localList;
	}
}
