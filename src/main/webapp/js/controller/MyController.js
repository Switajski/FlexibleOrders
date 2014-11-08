// Global Exception Handling
Ext.Ajax.on('requestexception', function(conn, response, options) {
			if (response.status === 400) {
				Ext.MessageBox.alert(response.status + ' Eingabefehler',
						response.responseText);
			} else if (response.status === 404) {
				Ext.MessageBox.alert(response.status + ' '
								+ response.statusText, options.url);
			} else if (response.status === 500) {
				Ext.MessageBox.alert('Schwerwiegender Fehler',
						response.responseText);
			} else {
				cause = response.status;
				if (response.timedout)
					cause = 'Request timed out';
				Ext.MessageBox.alert(response.statusText, cause);
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
	models : ['BestellungData', 'ItemData', 'KundeData', 'DeliveryMethodData'],
	stores : ['BestellungDataStore', 'ItemDataStore', 'KundeDataStore',
			'InvoiceItemDataStore', 'ShippingItemDataStore',
			'ArchiveItemDataStore', 'OrderNumberDataStore',
			'AgreementItemDataStore', 'DeliveryNotesItemDataStore',
			'InvoiceNumberDataStore', 'CreateOrderDataStore',
			'CreateDeliveryNotesItemDataStore', 'CreateInvoiceItemDataStore',
			'DeliveryNotesItemDataStore', 'DeliveryMethodDataStore',
			'CreateConfirmationReportItemDataStore',
			'DeliveryHistoryDataStore', 'CreateAgreementItemDataStore'],
	views : ['MainPanel', 'CreateCustomerWindow', 'BestellpositionGridPanel',
			'DeliveryHistoryPanel', 'AgreementItemGridPanel', 'ConfirmWindow',
			'DeliverWindow', 'AgreementWindow', 'OrderNumberComboBox',
			'InvoiceNumberComboBox', 'OrderWindow', 'InvoiceWindow',
			'DeliveryNotesItemGridPanel', 'DeliveryMethodComboBox'],
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
					},
					'#ShowSums' : {
						click : this.onShowSums
					}
				});


		this.getStore('ItemDataStore').filter('status', 'ordered');
		this.getStore('AgreementItemDataStore').filter('status', 'confirmed');
		this.getStore('ShippingItemDataStore').filter('status', 'agreed');
		this.getStore('DeliveryNotesItemDataStore').filter('status', 'shipped');
		this.getStore('InvoiceItemDataStore').filter('status', 'invoiced');

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

	retrieveChosenCustomerSavely : function() {
		customerNo = Ext.getCmp('mainCustomerComboBox').getValue();
		if (customerNo == 0 || customerNo == "" || customerNo == null) {
			Ext.MessageBox.show({
						title : 'Kundenfeld leer',
						msg : 'Bitte Kunden ausw&auml;hlen',
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
			return;
		}

		store = MyApp.getApplication().getStore('KundeDataStore');
		cIndex = store.findExact('customerNumber', customerNo);
		customer = store.getAt(cIndex);

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

	syncAll : function() {
		var allGrids = Ext.ComponentQuery.query('PositionGrid');
		allGrids.forEach(function(grid) {
					grid.getStore().load();
				});
	},

	onCustomerChange : function(field, newValue, oldValue, eOpts) {
		var stores = new Array();
		stores[0] = Ext.data.StoreManager.lookup('ItemDataStore');
		stores[1] = Ext.data.StoreManager.lookup('ShippingItemDataStore');
		stores[2] = Ext.data.StoreManager.lookup('AgreementItemDataStore');
		stores[3] = Ext.data.StoreManager.lookup('DeliveryNotesItemDataStore');
		stores[4] = Ext.data.StoreManager.lookup('InvoiceItemDataStore');

		stores.forEach(function(store) {
			found = false;
			store.filters.items.forEach(function(filter) {
						if (filter.property == MyApp.constants.FILTER_ON_CUSTOMER) {
							filter.value = newValue;
							found = true;
							store.load();
						}
					});
			if (!found) {
				store.filter(MyApp.constants.FILTER_ON_CUSTOMER, newValue);
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
						controller = MyApp.getApplication()
								.getController('MyController');
						controller.sleep(500);
						controller.syncAll();
					}
				});
	},

	onShowSums : function() {
		statesToGrids = [{
					state : 'agreed',
					grid : 'AgreementItemGrid',
					text : 'Auftr&auml;e'
				}, {
					state : 'confirmed',
					grid : 'ShippingItemGrid',
					text : 'Auftragsbest&auml;tigungen'
				}, {
					state : 'shipped',
					grid : 'DeliveryNotesItemGrid',
					text : 'Lieferscheine'
				}, {
					state : 'invoiced',
					grid : 'InvoiceItemGrid',
					text : 'Rechnungen'
				}],

		statesToGrids.forEach(function(stateToGrid) {
					Ext.Ajax.request({
								url : '/FlexibleOrders/statistics/openAmount',
								method : 'GET',
								params : {
									state : stateToGrid.state
								},
								success : function(response) {
									var text = response.responseText;
									shippedAmount = Ext.JSON.decode(text).data;
									Ext.getCmp(stateToGrid.grid)
											.setTitle(stateToGrid.text
													+ ' - Offener Betrag: '
													+ shippedAmount.value + ' '
													+ shippedAmount.currency);
								}
							});
				});
	}

});
