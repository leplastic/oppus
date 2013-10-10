package net.diogomarques.wifioppish;

import android.content.Context;
import android.net.wifi.WifiInfo;
import net.diogomarques.wifioppish.IEnvironment.State;

/**
 * Android implementation of state {@link IEnvironment.State#Scanning}
 * 
 * @author Diogo Marques <diogohomemmarques@gmail.com>
 */
public class StateScanning extends AState {

	public StateScanning(IEnvironment environment) {
		super(environment);
	}

	@Override
	public void start(int timeout, Context c) {
		final INetworkingFacade networking = environment.getNetworkingFacade();
		environment.deliverMessage("entered scanning state");		
		networking.scanForAP(timeout, new INetworkingFacade.OnAccessPointScanListener() {

			@Override
			public void onScanTimeout() {
				environment.deliverMessage("t_scan timeout");
				environment.gotoState(State.Beaconing);
			}

			@Override
			public void onAPConnection(String bSSID) {
				// calculate remote node ID
				String mac = bSSID;
				
				if(mac != null) {
					String remoteId = NodeIdentification.getNodeId(mac);
					environment.deliverMessage("connected to AP! (node ID is " + remoteId + " )");
				} else {
					environment.deliverMessage("connected to AP!");
				}
				
				environment.gotoState(State.Station);
			}
		});
	}
}
