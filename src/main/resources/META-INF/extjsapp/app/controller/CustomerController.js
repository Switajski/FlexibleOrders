Ext.define('MyApp.controller.CustomerController', {
	debug : true,
	extend : 'Ext.app.Controller',

	id : 'CustomerController',
	models : ['KundeData'],
	stores : ['KundeDataStore'],
	views : ['CreateCustomerWindow'],

	init : function(application) {
		this.control({
					'#CreateCustomerButton' : {
						click : this.onCreateCustomer
					}
				});
		console.log("CustomerController loaded");
	},
	
	onCreateCustomer : function(button, event, options) {
		console.log('onCreateCustomer');
		var createCustomerWindow = Ext.create('MyApp.view.CreateCustomerWindow', {
			id : "CreateCustomerWindow",
			onSave : function(){
				MyApp.getApplication().getController('CustomerController')
								.createCustomer("ok",
										Ext.data.StoreMgr.lookup('KundeDataStore'));
			}
		});
		
		createCustomerWindow.show();
	},
	
	createCustomer : function(event, record){
		form = Ext.getCmp('CustomerForm');
		var active = form.getForm().getRecord(), form;

		if (!active) {
			return;
		}
		
		if (form.isValid()) {
			form.updateRecord(active);
			var customerStore = Ext.data.StoreMgr.lookup('KundeDataStore');
			customerStore.add(form.getForm().getRecord());
			customerStore.sync({
				success : function (){
					form.getForm().reset();
					Ext.getCmp("CreateCustomerWindow").close();
				}
			});
		}
	}

});
