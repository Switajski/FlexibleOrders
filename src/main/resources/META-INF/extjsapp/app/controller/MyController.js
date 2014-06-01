// Global Exception Handling
Ext.Ajax.on('requestexception', function(conn, response, options) {
			if (response.status === 400) {
				Ext.MessageBox.alert('Eingabefehler', response.responseText);
			} else {
				Ext.MessageBox.alert('Server meldet Fehler',
						response.responseText);
			}
		});

Ext.override(Ext.data.JsonWriter, {
			encode : false,
			writeAllFields : true,
			listful : true,
			constructor : function(config) {
				this.callParent(this, config);
				return this;
			},
			render : function(params, baseParams, data) {
				params.jsonData = data;
			}
		});

Ext.define('MyApp.controller.MyController', {
	debug : true,
	extend : 'Ext.app.Controller',

	id : 'MyController',
	models : ['BestellungData', 'ItemData', 'KundeData'],
	stores : ['BestellungDataStore', 'ItemDataStore',
			'KundeDataStore', 'InvoiceItemDataStore', 'ShippingItemDataStore',
			'ArchiveItemDataStore', 'OrderNumberDataStore',
			'DeliveryNotesItemDataStore', 'InvoiceNumberDataStore',
			'CreateOrderDataStore', 'CreateDeliveryNotesItemDataStore',
			'CreateInvoiceItemDataStore', 'DeliveryNotesItemDataStore',
			'CreateConfirmationReportItemDataStore'],
	views : ['MainPanel', 'CreateCustomerWindow',
			'BestellpositionGridPanel',
			'ConfirmWindow', 'DeliverWindow',
			'OrderNumberComboBox', 'InvoiceNumberComboBox',
			'OrderWindow', 'InvoiceWindow', 'DeliveryNotesItemGridPanel'],
	// TODO: Registrieren und Initialisiseren von Views an einer Stelle
	// implementieren

	activeBestellnr : 0,
	activeBestellpositionId : 0,
	bestellungDataStore : null,

	activeCustomer : 0,

	init : function(application) {
		this.control({
					'#mainCustomerComboBox' : {
						change : this.onCustomerChange
					},
					'#DeleteBpButton' : {
						click : this.deleteBpDialog
					},
					'#DeleteBestellungButton' : {
						click : this.deleteBestellungDialog
					},
					'#SubmitBpButton' : {
						click : this.addBp
					},
					'#AbBestellungButton' : {
						click : this.bestaetigeAuftragDialog
					},
					'#BestellungPdf' : {
						click : this.showBestellundPdf
					},
					'#AbPdf' : {
						click : this.showAbPdf
					},
					'#RechnungPdf' : {
						click : this.showRechnungPdf
					},
					'#CreateShippingCostsButton' : {
						click : this.createShippingCosts
					},
					'#AddShippingCostsButton' : {
						click : this.addShippingCosts
					}
				});
		this.getStore('ItemDataStore').filter('status', 'ordered');
		this.getStore('ShippingItemDataStore').filter('status', 'confirmed');
		this.getStore('DeliveryNotesItemDataStore').filter('status', 'shipped');
		this.getStore('InvoiceItemDataStore').filter('status', 'invoiced');
		this.getStore('ArchiveItemDataStore').filter('status', 'completed');
	},

	onSelectionchange : function(view, selections, options) {
		if (view.getStore().storeId != "BestellungDataStore")
			return;
		if (selections.length == 0)
			return;
	},
	getButtons : function() {
		var buttons = {
			createCustomer : Ext.getCmp('CreateCustomerButton'),
			deleteBp : Ext.getCmp('DeleteBpButton'),
			erstelleBpButton : Ext.getCmp('ErstelleBpButton'),
			deleteBestellung : Ext.getCmp('DeleteBestellungButton'),
			erstelleBestellungButton : Ext.getCmp('ErstelleBestellungButton'),
			abBestellungButton : Ext.getCmp('AbBestellungButton'),
			rechnungBestellungButton : Ext.getCmp('RechnungBestellungButton'),
			bezahltBestellungButton : Ext.getCmp('BezahltBestellungButton'),
			stornoBestellungButton : Ext.getCmp('StornoBestellungButton'),
			deleteBestellungButton : Ext.getCmp('DeleteBestellungButton'),
			bestellungPdfButton : Ext.getCmp('BestellungPdf'),
			abPdfButton : Ext.getCmp('AbPdf'),
			rechnungPdfButton : Ext.getCmp('RechnungPdf'),
			offeneBpPdfButton : Ext.getCmp('OffeneBpPdf')
		};
		return buttons;
	},

	deleteBpDialog : function(button, event, options) {
		var bestellung = this.getBpSelection();

		if (bestellung.getData().status == 'ORDERED')
			Ext.MessageBox.confirm('Best&aumltigen',
					'Bestellpostion sicher l&ouml;schen?', this.deleteBp);
		else
			Ext.MessageBox
					.alert('Hinweis',
							'Bestellposition schon best&auml;tigt. Nur noch Storno ist m&ouml;glich.');
	},
	
	retrieveChosenCustomerSavely : function(){
		var customerId = Ext.getCmp('mainCustomerComboBox').getValue();
		if (customerId == 0 || customerId == "" || customerId == null) {
			Ext.MessageBox.show({
						title : 'Kundenfeld leer',
						msg : 'Bitte Kunden ausw&auml;hlen',
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
			return;
		}
		
		var customer = MyApp.getApplication().getStore('KundeDataStore')
				.getById(customerId);
				
		return customer;
	},

	deleteBestellungDialog : function(button, event, options) {
		var bestellung = this.getBestellungSelection();

		if (bestellung.getData().status == 'ORDERED')
			Ext.MessageBox.confirm('Best&aumltigen',
					'Bestellung sicher l&ouml;schen?', this.deleteBestellung);
		else
			Ext.MessageBox
					.alert('Hinweis',
							'Bestellung schon best&auml;tigt. Nur noch Storno ist m&ouml;glich.');
	},

	getBpSelection : function() {
		var bpGridPanel = Ext.getCmp('BestellpositionGridPanel');
		var selectionModel = bpGridPanel.getSelectionModel();
		var bp = selectionModel.getSelection()[0];
		return bp;
	},

	syncBpGrid : function(view, owner, options) {
		var bpDataStore = Ext.data.StoreManager
				.lookup('ItemDataStore');
	},

	showBestellundPdf : function(button, event, options) {
		var win = window.open('/leanorders/orders/' + this.activeBestellnr
						+ '.pdf', '_blank');
		win.focus();
	},
	showAbPdf : function(button, event, options) {
		var win = window.open('/leanorders/orderConfirmations/'
						+ this.activeBestellnr + '.pdf', '_blank');
		win.focus();
	},
	showRechnungPdf : function(button, event, options) {
		var win = window.open('/leanorders/invoices/' + this.activeBestellnr
						+ '.pdf', '_blank');
		win.focus();
	},
	sleep : function(milliseconds) {
		var start = new Date().getTime();
		for (var i = 0; i < 1e7; i++) {
			if ((new Date().getTime() - start) > milliseconds) {
				break;
			}
		}
	},

	deconfirm : function(event, ocnr, record) {
		store = Ext.getStore('ShippingItemDataStore');
		var request = Ext.Ajax.request({
					url : '/FlexibleOrders/transitions/cancelConfirmationReport',
					params : {
						confirmationNumber : ocnr
					},
					success : function(response) {
						store.remove(store.getGroups(ocnr).children);
					}
				});
	},

	withdraw : function(event, record) {
		if (event == "ok") {
			var request = Ext.Ajax.request({
						url : '/FlexibleOrders/transitions/cancelDeliveryNotes',
						params : {
							invoiceNumber : record.data.invoiceNumber
						},
						success : function(response) {
							store.remove(store.getGroups(ocnr).children);
						}
					});
		}
	},

	decomplete : function(event, record) {
		if (event == "ok") {

			var request = Ext.Ajax.request({
				url : '/FlexibleOrders/transitions/decomplete/json',
				params : {
					id : record.data.id,
					productNumber : record.data.product,
					orderConfirmationNumber : record.data.orderConfirmationNumber,
					invoiceNumber : record.data.invoiceNumber,
					accountNumber : record.data.accountNumber,
					quantity : record.data.quantity
				},
				success : function(response) {
					// TODO: make a responsive design
					var text = response.responseText;
				}
			});
			// TODO: DRY in Sync
			// Sync
			MyApp.getApplication().getController('MyController').sleep(500);
			var allGrids = Ext.ComponentQuery.query('PositionGrid');
			allGrids.forEach(function(grid) {
						grid.getStore().load();
					});
		}
	},

	complete : function(event, anr, record) {
		record.data.accountNumber = record.data.invoiceNumber
				.replace(/R/g, "Q");
		if (event == "ok") {

			var request = Ext.Ajax.request({
						url : '/FlexibleOrders/transitions/complete/json',
						params : {
							id : record.data.id,
							productNumber : record.data.product,
							quantity : record.data.quantity,
							invoiceNumber : record.data.invoiceNumber,
							accountNumber : record.data.accountNumber
						},
						jsonData : {
							invoiceNumber : record.data.invoiceNumber
						},
						success : function(response) {
							var text = response.responseText;
						}
					});

			// Sync
			MyApp.getApplication().getController('MyController').sleep(500);
			var allGrids = Ext.ComponentQuery.query('PositionGrid');
			allGrids.forEach(function(grid) {
						grid.getStore().load();
					});
		}
	},

	onCustomerChange : function(field, newValue, oldValue, eOpts) {
		var stores = new Array();
		stores[0] = Ext.data.StoreManager.lookup('ItemDataStore');
		stores[1] = Ext.data.StoreManager.lookup('ShippingItemDataStore');
		stores[2] = Ext.data.StoreManager.lookup('DeliveryNotesItemDataStore');
		stores[3] = Ext.data.StoreManager.lookup('InvoiceItemDataStore');
		stores[4] = Ext.data.StoreManager.lookup('ArchiveItemDataStore');

		stores.forEach(function(store) {
					found = false;
					store.filters.items.forEach(function(filter) {
								if (filter.property == 'customer') {
									filter.value = newValue;
									found = true;
									store.load();
								}
							});
					if (!found) {
						store.filter("customer", newValue);
						// store.load();
					}
				});
	},

	deleteReport : function(varDocumentNumber) {
		var request = Ext.Ajax.request({
			url : '/FlexibleOrders/transitions/deleteReport',
			params : {
				documentNumber : varDocumentNumber
			},
			success : function(response) {
				var text = response.responseText;
				// Sync
				MyApp.getApplication().getController('MyController').sleep(500);
			}
		});
	}

});
