Ext.define('MyApp.view.ArchiveItemGridPanel', {
			extend : 'MyApp.view.PositionGridPanel',
			alias : 'widget.ArchiveItemGrid',
			title : "Belege",
			customicon : '',
			onActionClick : function(view, a, b, column, event, record, f) {
				MyApp.getApplication().getController('MarkPaidController').markPaid(
						"ok", record);

			}

		});