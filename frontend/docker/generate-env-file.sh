#!/usr/bin/env sh

# prepare file for substitution
cp /usr/share/nginx/html/env-config.js /usr/share/nginx/html/env-config-template.js
sed -i 's/^\(window.\)\(.*\)=\(.*\)/\1\2="${\2}"/' /usr/share/nginx/html/env-config-template.js

# substitute environment variables
envsubst < /usr/share/nginx/html/env-config-template.js > /usr/share/nginx/html/env-config.js

# remove temporary file
rm -f /app/env-config-template.js