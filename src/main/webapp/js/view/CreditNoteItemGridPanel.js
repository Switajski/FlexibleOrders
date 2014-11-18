Ext.define('MyApp.view.CreditNoteItemGridPanel', {
	extend : 'MyApp.view.PositionGridPanel',
	alias : 'widget.CreditNoteItemGrid',
	title : "Gutschriften",
	id : 'CreditNoteItemGrid',
	customicon : '/FlexibleOrders/images/new_ab.png',
	onActionClick : function(view, a, b, column, event, record, f) {
				var anr = record.data.creditNoteNumber;
				
				MyApp.getApplication().getController('IssueController').onIssue(
						"ok", anr, record);

			},
	onRemoveClick: function(view, a, b, column, event, record, f) {
				MyApp.getApplication().getController('MyController').deleteReport(
						record.data.CreditNoteNumber);

			}


});