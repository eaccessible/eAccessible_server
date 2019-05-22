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
import bean.Caracteristica;
import bean.CaracteristicaTipoLocal;
import bean.CaracteristicaValor;


@WebService
public class ServeiWeb{
	@WebMethod
	public void altaLocal(Local local, List<Accessibilitat> accessibilitat) throws ExceptionController {
		
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		ex.setErrorCode(101);
		
		if(local.getCodicarrer() <= 0) {
			ex.setErrorMessage("No s'ha introduit un codi carrer valid");
			throw(ex);
		}if(local.getCodilocal() <= 0) {
			ex.setErrorMessage("No s'ha introduit un codi local valid");
			throw(ex);
		}if(local.getCoditipolocal() <= 0) {
			ex.setErrorMessage("No s'ha introduit un codi tipo local valid");
			throw(ex);
		}if(local.getNomcarrer().isEmpty()) {
			ex.setErrorMessage("No s'ha introduit un nom de carrer valid");
			throw(ex);
		}if(local.getNomlocal().isEmpty()) {
			ex.setErrorMessage("No s'ha introduit un nom de local valid");
			throw(ex);
		}if(local.getNomvia().isEmpty()) {
			ex.setErrorMessage("No s'ha introduit un nom de via valid");
			throw(ex);
		}if(local.getNumero() <= 0) {
			ex.setErrorMessage("No s'ha introduit un numero valid");
			throw(ex);
		}
		
		ex.setErrorCode(102);
		for(int i=0; i<accessibilitat.size(); i=i+1) {
			if(accessibilitat.get(i).getCodiaccessibilitat() <= 0) {
				ex.setErrorMessage("No s'ha introduit un codi d'accessibilitat valid");
				throw(ex);
			}
			if(accessibilitat.get(i).getCodicaracteristica() <= 0) {
				ex.setErrorMessage("No s'ha introduit un codi de caracteristica valid");
				throw(ex);
			}if(accessibilitat.get(i).getCodilocal() <= 0) {
				ex.setErrorMessage("No s'ha introduit un codi de local valid");
				throw(ex);
			}
			if(accessibilitat.get(i).getValor() < 0 && accessibilitat.get(i).getValor() > 5) {
				ex.setErrorMessage("No s'ha introduit un valor valid");
				throw(ex);
			}
			if(accessibilitat.get(i).getCodilocal() != local.getCodilocal()) {
				ex.setErrorCode(103);
				ex.setErrorMessage("S'ha detectat una referencia a un local inadequat");
				throw(ex);
			}
			if(accessibilitat.get(i).getCodilocal() != local.getCodilocal()) {
				ex.setErrorCode(103);
				ex.setErrorMessage("S'ha detectat una referencia a un local inadequat");
				throw(ex);
			}
		}
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(104);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");

					throw(ex);
				}
				else{
					connection = ds.getConnection();
					
					String query = "insert into eAccessible.local (codilocal,coditipolocal,codicarrer,nomcarrer,nomvia,numero,nomlocal,observacions,verificat) values('"+local.getCodilocal()+"','"+local.getCoditipolocal()+"','"+local.getCodicarrer()+"','"+local.getNomcarrer()+"','"+local.getNomvia()+"','"+local.getNumero()+"',UPPER('"+local.getNomlocal()+"'),'"+local.getObservacions()+"','"+local.getVerificat()+"')";
					Statement stm = connection.createStatement();
					stm.executeUpdate(query);
					
					for(int i=0; i<accessibilitat.size(); i=i+1) {
						stm.executeUpdate("insert into eAccessible.accessibilitat (codiaccessibilitat,codilocal,codicaracteristica,valor,verificat) values('"+accessibilitat.get(i).getCodiaccessibilitat()+"','"+accessibilitat.get(i).getCodilocal()+"','"+accessibilitat.get(i).getCodicaracteristica()+"','"+accessibilitat.get(i).getValor()+"','"+accessibilitat.get(i).getVerificat()+"')");
					}
					
					connection.close();
					stm.close();
				}
			}
		}
		catch(Exception e) {	
			e.printStackTrace();
			ex.setErrorCode(105);
			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);
			
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

	public void validaLocal (int codiLocal) throws ExceptionController{

		String strEstat = new String();
		Connection connection = null;
		ExceptionController ex = new ExceptionController();
		ex.setErrorCode(201);
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(204);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}
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
		catch(Exception e) {
			e.printStackTrace();
			ex.setErrorCode(205);

			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);

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
	public void baixaLocal (int codiLocal) throws ExceptionController {
		String strEstat = new String();
		Connection connection = null;
		ExceptionController ex = new ExceptionController();
		
		
		try{
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(304);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");

					throw(ex);
				}
				else{
					connection = ds.getConnection();
					String query = "delete from eAccessible.accessibilitat where codilocal="+codiLocal;
					Statement stm = connection.createStatement();
					stm.executeUpdate(query);
					
					query = "delete from eAccessible.local where codilocal="+codiLocal;
					stm.executeUpdate(query);
					
					connection.close();
					stm.close();
				}
			}
		}

		catch(Exception e) {
			e.printStackTrace();
			ex.setErrorCode(305);
			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);

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
	public List<Local> infoLocalPerNomLocalICodiTipoLocal(String nomLocal, int codiTipoLocal) throws ExceptionController{
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		
		List<Local> localList = new ArrayList<Local>();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(904);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}	
				else{
					connection = ds.getConnection();
					
					String query = "select coditipolocal,codicarrer,nomcarrer,nomvia,codilocal,nomlocal,numero,observacions,verificat from eAccessible.local where nomlocal LIKE UPPER('%"+nomLocal+"%') AND coditipolocal="+codiTipoLocal ;
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
		catch(Exception e) {
			e.printStackTrace();
			ex.setErrorCode(905);
			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);
		}
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
	public Local infoLocalPerCodiLocal(int codiLocal) throws ExceptionController{
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		
		Local local = new Local();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(404);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}	
				else{
					connection = ds.getConnection();
					
					String query = "select coditipolocal,codicarrer,nomcarrer,nomvia,codilocal,nomlocal,numero,observacions,verificat from eAccessible.local where codilocal="+codiLocal;
					Statement stm = connection.createStatement();
					ResultSet rs = stm.executeQuery(query);
					rs.next();
					local.setCoditipolocal(rs.getInt("coditipolocal"));
					local.setCodicarrer(rs.getInt("codicarrer"));
					local.setNomcarrer(rs.getString("nomcarrer"));
					local.setNomvia(rs.getString("nomvia"));
					local.setCodilocal(rs.getInt("codilocal"));
					local.setNomlocal(rs.getString("nomlocal"));
					local.setNumero(rs.getInt("numero"));
					local.setObservacions(rs.getString("observacions"));
					local.setVerificat(rs.getString("verificat"));
					
					connection.close();
					stm.close();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			ex.setErrorCode(405);
			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);
		}
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
	public List<Local> infoLocalPerNomLocal(String nomLocal) throws ExceptionController{
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		
		List<Local> localList = new ArrayList<Local>();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(404);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}	
				else{
					connection = ds.getConnection();
					
					String query = "select coditipolocal,codicarrer,nomcarrer,nomvia,codilocal,nomlocal,numero,observacions,verificat from eAccessible.local where nomlocal LIKE UPPER('%"+nomLocal+"%')";
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
		catch(Exception e) {
			e.printStackTrace();
			ex.setErrorCode(405);
			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);
		}
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
	public List<Local> infoLocalPerTipoLocal(int codiTipoLocal) throws ExceptionController{
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		
		List<Local> localList = new ArrayList<Local>();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(804);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}	
				else{
					connection = ds.getConnection();
					
					String query = "select coditipolocal,codicarrer,nomcarrer,nomvia,codilocal,nomlocal,numero,observacions,verificat from eAccessible.local where coditipolocal="+codiTipoLocal;
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
		catch(Exception e) {
			e.printStackTrace();
			ex.setErrorCode(805);
			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);
		}
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
	public List<TipoLocal> cercaTipoLocal() throws ExceptionController{
				
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		
		List<TipoLocal> tipoLocalList = new ArrayList<TipoLocal>();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(504);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}
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
		catch(Exception e) {
			e.printStackTrace();
			ex.setErrorCode(505);
			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);
		}
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
	public List<Local> localnoVerificat() throws ExceptionController{
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();

		List <Local> localList = new ArrayList <Local>();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(604);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}
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
		catch(Exception e) {	
			e.printStackTrace();
			ex.setErrorCode(605);
			ex.setErrorMessage("S'ha produit un error a la base de dades");	
		}
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
	public List<Local> localsAccessibles(int codiCaracteristica) throws ExceptionController {
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		
		List <Local> localList = new ArrayList <Local>();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(704);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}
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
		catch(Exception e) {	
			e.printStackTrace();
			ex.setErrorCode(705);
			ex.setErrorMessage("S'ha produit un error a la base de dades");	
		}
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
	public List<CaracteristicaTipoLocal>  infoCaracteristicaTipoLocal(int codiTipoLocal) throws ExceptionController{
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		
		List<CaracteristicaTipoLocal> caracteristicaTipoLocalList = new ArrayList<CaracteristicaTipoLocal>();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(1004);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}
				else{
					connection = ds.getConnection();
					
					String query = "select codicaracteristicatipolocal, codicaracteristica, coditipolocal from eAccessible.caracteristicatipolocal where caracteristicatipolocal.coditipolocal="+codiTipoLocal;
					Statement stm = connection.createStatement();
					ResultSet rs = stm.executeQuery(query);
					while(rs.next()) {
						CaracteristicaTipoLocal caracteristicaTipoLocal = new CaracteristicaTipoLocal();
						caracteristicaTipoLocal.setCodicaracteristicatipolocal(rs.getInt("codicaracteristicatipolocal"));
						caracteristicaTipoLocal.setCodicaracteristica(rs.getInt("codicaracteristica"));
						caracteristicaTipoLocal.setCoditipolocal(rs.getInt("coditipolocal"));
						caracteristicaTipoLocalList.add(caracteristicaTipoLocal);
					}
					connection.close();
					stm.close();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			ex.setErrorCode(1005);
			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);
		}
		finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return caracteristicaTipoLocalList;
	}
	
	@WebMethod
	public Caracteristica  infoCaracteristica(int codiCaracteristica) throws ExceptionController{
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		
		Caracteristica caracteristica = new Caracteristica();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(1104);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}
				else{
					connection = ds.getConnection();
					
					String query = "select codicaracteristica, nomcaracteristicaca, nomcaracteristicaes, nomcaracteristicaen, tipo, codinivell  from eAccessible.caracteristica where caracteristica.codicaracteristica="+codiCaracteristica;
					Statement stm = connection.createStatement();
					ResultSet rs = stm.executeQuery(query);
					rs.next();
					
					caracteristica.setCodicaracteristica(rs.getInt("codicaracteristica"));
					caracteristica.setNomcaracteristicaca(rs.getString("nomcaracteristicaca"));
					caracteristica.setNomcaracteristicaes(rs.getString("nomcaracteristicaes"));
					caracteristica.setNomcaracteristicaen(rs.getString("nomcaracteristicaen"));
					caracteristica.setTipo(rs.getInt("tipo"));
					caracteristica.setCodinivell(rs.getInt("codinivell"));
					
					connection.close();
					stm.close();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			ex.setErrorCode(1105);
			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);
		}
		finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return caracteristica;
	}
	
	
	
	@WebMethod
	public int codiLocalLliure() throws ExceptionController{
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		
		int lastCodiLocal = 0;
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(1204);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}
				else{
					connection = ds.getConnection();
					
					String query = "select MAX(codilocal) codilocal  from eAccessible.local";
					Statement stm = connection.createStatement();
					ResultSet rs = stm.executeQuery(query);
					rs.next();
					lastCodiLocal = rs.getInt("codilocal");
				
					connection.close();
					stm.close();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			ex.setErrorCode(1205);
			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);
		}
		finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lastCodiLocal+1;
		
	}
	
	@WebMethod
	public List<CaracteristicaValor>  infoCaracteristicaLocal(int codiLocal) throws ExceptionController{
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		
		List<CaracteristicaValor> caracteristicaValorLlista = new ArrayList<CaracteristicaValor>();
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(1004);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}
				else{
					connection = ds.getConnection();
					
					String query = "select accessibilitat.valor valor, caracteristica.nomcaracteristicaca nomcaracteristicaca, accessibilitat.codilocal, accessibilitat.codicaracteristica codicaracteristica from eAccessible.local INNER JOIN eAccessible.accessibilitat ON local.codilocal = accessibilitat.codilocal INNER JOIN eAccessible.caracteristica ON accessibilitat.codicaracteristica = caracteristica.codicaracteristica AND local.codilocal="+codiLocal;
					Statement stm = connection.createStatement();
					ResultSet rs = stm.executeQuery(query);
					while(rs.next()) {
						CaracteristicaValor caracteristicaValor = new CaracteristicaValor();
						caracteristicaValor.setNomcaracteristicaca(rs.getString("nomcaracteristicaca"));
						caracteristicaValor.setValor(rs.getInt("valor"));
						caracteristicaValorLlista.add(caracteristicaValor);
					}
					connection.close();
					stm.close();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			ex.setErrorCode(1005);
			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);
		}
		finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return caracteristicaValorLlista;
	}

	
	public int  codiAccessibilitatLliure() throws ExceptionController{
		String strEstat = new String();
		Connection connection = null;
		
		ExceptionController ex = new ExceptionController();
		
		int lastCodiAccessibilitat = 0;
		
		try{	
			InitialContext cxt = new InitialContext();
			if ( cxt != null ){
				DataSource ds = (DataSource) cxt.lookup( "java:jboss/PostgreSQL/eAccessible");
				if ( ds == null ) {
					strEstat = "Error al crear el datasource";
					ex.setErrorCode(1304);
					ex.setErrorMessage("No hi ha connexio amb la base de dades");
					throw(ex);
				}
				else{
					connection = ds.getConnection();
					
					String query = "select MAX(codiAccessibilitat) codiAccessibilitat  from eAccessible.accessibilitat";
					Statement stm = connection.createStatement();
					ResultSet rs = stm.executeQuery(query);
					rs.next();
					lastCodiAccessibilitat = rs.getInt("codiAccessibilitat");
				
					connection.close();
					stm.close();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			ex.setErrorCode(1305);
			ex.setErrorMessage("S'ha produit un error a la base de dades");
			throw(ex);
		}
		finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lastCodiAccessibilitat+1;
	}
	
}
