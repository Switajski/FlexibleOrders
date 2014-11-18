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
					},
					'[id=CustomerForm] field': {
			            change: function() {
							var window = Ext.getCmp("CreateCustomerWindow");
							if (window != null)
								window.down('form').getForm()
									.updateRecord(window.record);
			            }
			        },
			        '#CreateCustomerWindow' : {
			        	close : this.onClose
			        }
				});
	},
	
	onCreateCustomer : function(button, event, options) {
		var createCustomerWindow = Ext.create('MyApp.view.CreateCustomerWindow', {
			id : "CreateCustomerWindow",
			onSave : function(){
				MyApp.getApplication().getController('CustomerController')
								.createCustomer(
										Ext.data.StoreMgr.lookup('KundeDataStore'));
			}
		});
		
		createCustomerWindow.down('[form]').getForm().reset()
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
			store.add(active);
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
			title : 'Kunden bearbeiten',
			customerNumberEditable : false,
			onSave : function(){
				form2 = this.down('form').getForm();
				form2.updateRecord();
				MyApp.getApplication().getController('CustomerController')
								.updateCustomer(
										Ext.data.StoreMgr.lookup('KundeDataStore'),
										customer);
			}
		});
		createCustomerWindow.down('field[name=customerNumber]').setDisabled('true');
		createCustomerWindow.down('form').getForm().loadRecord(customer);
		createCustomerWindow.show();
		
		this.displayAmounts(createCustomerWindow);
	},
	
	displayAmounts : function(window){
		amountRequests = [{
			state : 'confirmed',
			fieldQuery : 'field[name=openOrderConfirmation]'
		}, {
			state : 'shipped',
			fieldQuery : 'field[name=openDeliveryNotes]'
		}, {
			state : 'invoiced',
			fieldQuery : 'field[name=openInvoices]'
		}];

		amountRequests.forEach(function(amountRequest) {
			Ext.Ajax.request({
				url : '/FlexibleOrders/statistics/openAmount',
				method : 'GET',
				params : {
					state : amountRequest.state
				},
				success : function(response) {
					var text = response.responseText;
					shippedAmount = Ext.JSON.decode(text).data;
					window.down(amountRequest.fieldQuery)
							.setValue(shippedAmount.value + '€');
				}
			});
		});
	},
	
	updateCustomer : function(store, customer){
		cIndex = store.findExact('customerNumber', customerNo);
		recToUpdate = store.getAt(cIndex);
		
		recToUpdate.set(customer);
		store.sync({
				success : function (){
					Ext.getCmp('CustomerForm').getForm().reset();
					Ext.getCmp("CreateCustomerWindow").close();
				}
			});
	},
	
	onClose : function(){
		form = Ext.getCmp("CreateCustomerWindow").down('form').getForm();
		form.getRecord().reject();
		form.reset();
	}

});
