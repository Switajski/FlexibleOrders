Ext.define('MyApp.controller.AgreementController', {
	debug : true,
	extend : 'Ext.app.Controller',

	id : 'AgreementController',
	models : ['ItemData'],
	stores : ['CreateAgreementItemDataStore'],
	views : ['AgreementWindow'],

	init : function(application) {
		this.control({
				});
	},
	
	onAgree : function(event, record) {
		agreementNumber = record.data.documentNumber.replace(/AB/g, "AU");

		record.data.agreementNumber = record.data.documentNumber;
		var createAgreementStore = MyApp.getApplication()
				.getStore('CreateAgreementItemDataStore');
		createAgreementStore.filter([{
					property : "customerNumber",
					value : record.data.customerNumber
				}, {
					property : "status",
					value : "agreed"
				}]);

		var agreementWindow = Ext.create('MyApp.view.AgreementWindow', {
					id : "AgreementWindow",
					onSave : function() {
						MyApp
								.getApplication()
								.getController('AgreeController')
								.agree("ok", kunde, createAgreementStore);
					}
				});
		kunde = Ext.getStore('KundeDataStore').findRecord("customerNumber",
				record.data.customerNumber);
		kundeId = kunde.data.id;
		email = kunde.data.email;
console.log('programming here');
		agreementWindow.show();
		agreementWindow.down('form').getForm().setValues({
				customerNumber : kunde.data.customerNumber,
				lastName : kunde.data.lastName,
				expectedDelivery : kunde.data.expectedDelivery,
				orderConfirmationNumber : record.data.documentNumber,
				agreementNumber : agreementNumber
				});
		// somehow the id is deleted onShow
		//Ext.getCmp('agreementNumber').setValue(agreementNumber);
		//Ext.getStore('KundeDataStore').findRecord("email", email).data.id = kundeId;
	},

	agree : function(event, record, createAgreementStore) {
		var form = Ext.getCmp('AgreementWindow').down('form').getForm();
		if (event == "ok") {

			var request = Ext.Ajax.request({
				url : '/FlexibleOrders/transitions/agree/json',
				// headers: { 'Content-Type': 'application/json' },
				jsonData : {
					orderAgreementNumber : form.getValues().agreementNumber
				},
				success : function(response) {
					var text = response.responseText;
					// Sync
					MyApp.getApplication().getController('MyController')
							.sleep(500);
					var allGrids = Ext.ComponentQuery.query('PositionGrid');
					allGrids.forEach(function(grid) {
								grid.getStore().load();
							});
					Ext.getCmp("AgreementWindow").close();
				}
			});
		}
	}
	
	
});