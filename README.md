# VERT-X AT

## VertxAT is an implementation of the AT Structures for Vertx

### A PROJECT UNDER THE ΙΔΕΑ STATEMENT

Current supported versions : 3.8.2, 3.8.1, 3.8.0, 3.7.1 , 3.7.0, 3.6.3, 3.6.2, 3.6.1, 3.6.0


Steps to execute :

1. Build the VertX version you want to test
2. export VERTX_VERSION='version of VertX branch'
3. export JBOSS_VERSION='version of VertX branch ending with Final', for example if VERTX_VERSION=4.0.0-SNAPSHOT then JBOSS_VERSION=4.0.0.Final-SNAPSHOT
4. mvn clean install -Dmaster

## License

Code distributed under [ASL 2.0](LICENSE.TXT)(licenses of the Vertx test sources) and [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html) and [Eclipse Public License 2.0](http://www.eclipse.org/legal/epl-2.0) (for the repo).
