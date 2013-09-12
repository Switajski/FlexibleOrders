// Gridpanel with option to add orderitems
Ext.define('MyApp.view.BestellungGridPanel', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.bestellunggridpanel',
	flex : 2,
	id : 'BestellungGridPanel',
	store : 'BestellungDataStore',
	emptyText : 'Keine Bestellungen gefunden',
	viewConfig : {
		id : 'BestellungGrid'
	},
	columns : [{
				xtype : 'gridcolumn',
				dataIndex : 'orderNumber',
				text : 'Bestellnr'
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'customer',
				text : 'Kunde'
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'created',
				text : 'Bestelldatum'
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'netAmount',
				text : 'Betrag'
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'status',
				text : 'Status'
			}, {
				xtype : 'gridcolumn',
				dataIndex : 'size',
				text : 'Positionen'
			}],
	dockedItems : [{
				xtype : 'pagingtoolbar',
				dock : 'bottom',
				width : 360,
				displayInfo : true,
				store : 'BestellungDataStore'
			}, {
				xtype : 'toolbar',
				items : [{
							id : 'ErstelleBestellungButton',
							icon : '/FlexibleOrders/images/add.png',
							text : 'erstelle',
							scope : this
						}, {
							id : 'AbBestellungButton',
							text : 'Auftrag best&auml;tigen',
							icon : '/FlexibleOrders/images/new_ab.png',
							tooltip : 'Auftrag bestaetigen. Damit kommen die Bestellpositionen unter offene Posten',
							disabled : true,
							schope : this
						}, {
							id : 'RechnungBestellungButton',
							text : 'Rechnung erstellen',
							icon : '/FlexibleOrders/images/new_rechnung.png',
							tooltip : 'Rechnung in Pdf erstellen und aus den offenen Posten herausnehmen.',
							disabled : true,
							schope : this
						}, {
							id : 'BezahltBestellungButton',
							text : 'Als Bezahlt markieren',
							icon : '/FlexibleOrders/images/bezahlt.png',
							tooltip : 'Bestellung als bezahlt markieren',
							disabled : true,
							schope : this
						}, {
							id : 'StornoBestellungButton',
							icon : '/FlexibleOrders/images/delete_task.png',
							text : 'stornieren',
							tooltip : 'Die Bestellung bleibt in der Datenbank, wird allerdings als "Storniert" markiert',
							disabled : true,
							scope : this
						}, {
							id : 'DeleteBestellungButton',
							icon : '/FlexibleOrders/images/delete.png',
							text : 'l&ouml;schen',
							tooltip : 'Bestellung wird engdg&uuml;ltig aus der Datenbank gel&ouml;scht.',
							disabled : true,
							scope : this
						}, {
							xtype : 'tbseparator'
						}, {
							xtype : 'button',
							text : 'PDF',
							icon : '/FlexibleOrders/images/pdf_button.png',
							menu : {
								xtype : 'menu',
								minWidth : 120,
								items : [{
											id : 'BestellungPdf',
											xtype : 'menuitem',
											text : 'Bestellung',
											disabled : true
										}, {
											id : 'AbPdf',
											xtype : 'menuitem',
											text : 'Auftragsbest&auml;tigung',
											disabled : true
										}, {
											id : 'RechnungPdf',
											xtype : 'menuitem',
											text : 'Rechnung',
											disabled : true
										}, {
											xtype : 'menuseparator'
										}, {
											id : 'OffeneBpPdf',
											xtype : 'menuitem',
											text : 'Offene Bestellpositionen',
											disabled : true
										}]
							}
						}]
			}]
});