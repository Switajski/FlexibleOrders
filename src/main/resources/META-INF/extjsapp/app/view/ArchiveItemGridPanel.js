Ext.define('MyApp.view.ArchiveItemGridPanel', {
			extend : 'MyApp.view.PositionGridPanel',
			alias : 'widget.ArchiveItemGrid',
			title : "Belege",
			customicon : '',
			onActionClick : function(view, a, b, column, event, record, f) {
				console.log('ArchiveItemGrid - customtransitionfunction');
				MyApp.getApplication().getController('MyController').complete(
						"ok", record);

			}

		});