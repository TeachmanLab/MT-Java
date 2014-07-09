This directory will be populated by bower with various Javascript and CSS
dependencies.  Please run `bower install` to install these.  Project specific
files should not be placed here, but rather in the /src/main/webapp directory,
where they can be managed independently.

The grepfrut directory contains the Grepfrut Responsive Design template, available
from Themeforest.  Updats to this theme can be downloaded and dropped into place here.
We have some technical debt here in pulling out the modifications to these
files, and placing them in separate css file.

PIPlayer should Only be managed by bower, from the root directory. Nothing in
that directory should get checked in to the repository.

HOWEVER - currently the file PIPlayer/dist/js/config.js MUST be manually edited
after running bower install, the baseURL must be changed from "js" to "PIPlayer/dist/js"
I'd love to find a way to correct for this.