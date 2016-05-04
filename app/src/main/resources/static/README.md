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