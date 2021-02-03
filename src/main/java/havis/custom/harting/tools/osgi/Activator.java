package havis.custom.harting.tools.osgi;

import havis.custom.harting.tools.rest.RESTApplication;

import javax.ws.rs.core.Application;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {
	private ServiceRegistration<Application> app;
	
	@Override
	public void start(BundleContext context) throws Exception {
		app = context.registerService(Application.class, new RESTApplication(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (app != null) {
			app.unregister();
			app = null;
		}
	}
}
