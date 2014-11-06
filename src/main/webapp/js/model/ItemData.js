Ext.define('MyApp.model.ItemData', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'id'
					}, {
						name : 'product',
						type : 'int'
					}, {
						name : 'productName'
					}, {
						name : 'customer',
						type : 'int'
					}, {
						name : 'customerName'
					}, {
						name : 'customerNumber'
					}, {
						name : 'orderNumber'
					}, {
						name : 'invoiceNumber'
					}, {
						name : 'orderConfirmationNumber'
					}, {
						name : 'deliveryNotesNumber'
					}, {
						name : 'documentNumber'
					}, {
						name : 'receiptNumber'
					}, {
						name : 'quantity',
						type : 'int'
					}, {
						name : 'quantityLeft',
						type : 'int'
					}, {
						name : 'priceNet'
					}, {
						name : 'productType'
					},{
						name : 'status'
					}, {
						name : 'expectedDelivery',
						format : 'd/m/Y'
					}, {
						name : 'created',
						format : 'd/m/Y'
					},// DeliveryNotes
					{
						name : 'trackNumber'
					}, {
						name : 'packageNumber'
					}, {
						name : 'shareHistory'
					}, {
						name : 'agreed'
					}
			]
		});