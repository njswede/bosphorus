#!/bin/sh
if [ -z "$VRATENANT" ]; then
	VRATENANT=vsphere.local
fi
/usr/bin/java -jar `ls /opt/bosphorus/bosphorus*.jar` --vra.url=$VRAURL --vra.tenant=$VRATENANT
