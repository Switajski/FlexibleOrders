Ext.define('MyApp.view.ArchiveItemGridPanel', {
			extend : 'MyApp.view.PositionGridPanel',
			alias : 'widget.ArchiveItemGrid',
			title : "Belege",
			customicon : '',
			onActionClick : function(view, a, b, column, event, record, f) {
				console.log('ArchiveItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController').complete(
						"ok", record);

			},
			onPdfClick : function(view, a, b, column, event, record, f) {
				console.info("notImplemented");
			},
			onRemoveClick : function(view, a, b, column, event, record, f) {
				console.log('ArchiveItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController').decomplete(
						"ok", record);

			}

		});