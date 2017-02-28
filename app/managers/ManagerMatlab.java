package app.managers;
import java.util.Observable;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;


public class ManagerMatlab extends Observable {
	
	private static ManagerMatlab instance = null;
	
	private ManagerMatlab() {};
	
	public static ManagerMatlab getInstance() {
		if (ManagerMatlab.instance == null) {
			return new ManagerMatlab();
		}
		return instance;
	}
	
	/*
	
	private MatlabProxy proxy;
	private MatlabProxyFactory factory;
	
	public ManagerMatlab() {
		ManagerMatlab.instance = this;
		

		 //Lancement de matlab s'il n'est pas lancé, sinon création d'un tube de connexion entre les deux
		
		ManagerMatlab.instance.factory = new MatlabProxyFactory(); 
		try {
			ManagerMatlab.instance.proxy = ManagerMatlab.instance.factory.getProxy();
		} catch (MatlabConnectionException e) {
			
			 // Erreur lors de la création du tube, on coupe le programme

			System.exit(-1);
		}
		
	}
	
	public static void close() {
		ManagerMatlab.getInstance().getProxy().disconnect();
	}

	public MatlabProxy getProxy() {
		return proxy;
	}

	public MatlabProxyFactory getFactory() {
		return factory;
	}
	
	public void putValueInt(String var, int value) throws MatlabInvocationException {
		ManagerMatlab.getInstance().proxy.setVariable(var, value);
	}
	
	*/
}
