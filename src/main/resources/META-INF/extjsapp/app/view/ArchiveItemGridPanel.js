Ext.define('MyApp.view.ArchiveItemGridPanel', {
	extend : 'MyApp.view.PositionGridPanel',
	alias : 'widget.ArchiveItemGrid',
	title : "Archivierte/Bezahlte Positionen - Archive items",
	customicon : '',
	onPdfClick : function(view, a, b, column, event, record, f){
				var win = window.open('/FlexibleOrders/archiveitems/' + record.data.accountNumber
						+ '.pdf', '_blank');
				win.focus();
			}

});