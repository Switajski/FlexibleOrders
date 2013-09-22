Ext.define('MyApp.view.ArchiveItemGridPanel', {
			extend : 'MyApp.view.PositionGridPanel',
			alias : 'widget.ArchiveItemGrid',
			title : "Abgeschlossene Positionen",
			customicon : '',
			onActionClick : function(view, a, b, column, event, record, f) {
				console.log('ArchiveItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController').complete(
						"ok", record);

			},
			onPdfClick : function(view, a, b, column, event, record, f) {
				var win = window.open('/FlexibleOrders/archiveitems/'
								+ record.data.accountNumber + '.pdf', '_blank');
				win.focus();
			},
			onRemoveClick : function(view, a, b, column, event, record, f) {
				console.log('ArchiveItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController').decomplete(
						"ok", record);

			}

		});