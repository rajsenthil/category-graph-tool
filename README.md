# Category Graph Tool
This tool is used to load [google's product category definition](http://www.google.com/basepages/producttype/taxonomy-with-ids.en-US.xls) into Janugraph
This tool use spring-batch with customized line-mapper, field-set mapper, to support 
variations.

## To Start:
To keep it simple, convert the [google's product category spreadsheet](http://www.google.com/basepages/producttype/taxonomy-with-ids.en-US.xls) from xls to csv.
This will avoid using any special library like apache poi to external tool.


