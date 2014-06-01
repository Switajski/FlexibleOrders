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
					},
					'#UpdateCustomerButton' : {
						click : this.onUpdateCustomer
					}
				});
	},
	
	onCreateCustomer : function(button, event, options) {
		console.log('onCreateCustomer');
		var createCustomerWindow = Ext.create('MyApp.view.CreateCustomerWindow', {
			id : "CreateCustomerWindow",
			onSave : function(){
				MyApp.getApplication().getController('CustomerController')
								.createCustomer(
										Ext.data.StoreMgr.lookup('KundeDataStore'));
			}
		});
		
		createCustomerWindow.show();
	},
	
	createCustomer : function(store){
		form = Ext.getCmp('CustomerForm');
		var active = form.getForm().getRecord(), form;

		if (!active) {
			return;
		}
		
		if (form.isValid()) {
			form.updateRecord(active);
			store.add(form.getForm().getRecord());
			store.sync({
				success : function (){
					form.getForm().reset();
					Ext.getCmp("CreateCustomerWindow").close();
				}
			});
		}
	},
	
	onUpdateCustomer : function(button, event, options) {
		var customer = MyApp.getApplication().getController('MyController')
			.retrieveChosenCustomerSavely();
		if (customer == null)
			return;
			
		var createCustomerWindow = Ext.create('MyApp.view.CreateCustomerWindow', {
			id : "CreateCustomerWindow",
			customerNumberEditable : false,
			createCustomerRecord : function() {return customer},
			onSave : function(){
				MyApp.getApplication().getController('CustomerController')
								.updateCustomer(
										Ext.data.StoreMgr.lookup('KundeDataStore'),
										this.down('form').getForm().getRecord());
			}
		});
		createCustomerWindow.down('form').getForm().loadRecord(customer);
		createCustomerWindow.show();
	},
	
	updateCustomer : function(store, record){
		updatedRecord = Ext.getCmp("CreateCustomerWindow").record; //changed record
		var record = store.findRecord('customerNumber', Ext.getCmp("CreateCustomerWindow").record.data.customerNumber);
		//FIXME: Next code lines are just a workaround until real record gets updated by form listeners of CreateCustomerWindow 
		record.set('email', updatedRecord.data.email);
		record.set('firstName', updatedRecord.data.firstName);
		record.set('lastName', updatedRecord.data.lastName);
		record.set('name1', updatedRecord.data.name1);
		record.set('name2', updatedRecord.data.name2);
		record.set('street', updatedRecord.data.street);
		record.set('postalCode', updatedRecord.data.postalCode)
		record.set('city', updatedRecord.data.city);
		store.sync({
				success : function (){
					Ext.getCmp('CustomerForm').getForm().reset();
					Ext.getCmp("CreateCustomerWindow").close();
				}
			});
	}

});
