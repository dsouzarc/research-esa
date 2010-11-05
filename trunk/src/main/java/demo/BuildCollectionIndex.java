package demo;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import edu.kit.aifb.ConfigurationException;
import edu.kit.aifb.ConfigurationManager;
import edu.kit.aifb.document.ICollection;
import edu.kit.aifb.nlp.Language;
import edu.kit.aifb.terrier.TerrierIndexFactory;

public class BuildCollectionIndex {

	static Logger logger = Logger.getLogger( BuildCollectionIndex.class );

	static final String[] REQUIRED_PROPERTIES = {
		"collection_bean",
		"language",
		"index_id",
	};
	
	static public void main( String[] args ) throws Exception {
		try {
			ApplicationContext context = new FileSystemXmlApplicationContext( "*_context.xml" );
			ConfigurationManager confMan = (ConfigurationManager) context.getBean( ConfigurationManager.class );
			confMan.parseArgs( args );
			confMan.checkProperties( REQUIRED_PROPERTIES );
			Configuration config = confMan.getConfig();

			String indexId = config.getString( "index_id" );
			Language language = Language.getLanguage( config.getString( "language" ) );

			ICollection collection = (ICollection) context.getBean(
					config.getString( "collection_bean" ) );

			TerrierIndexFactory factory = (TerrierIndexFactory) context.getBean(
					TerrierIndexFactory.class );
			
			factory.buildIndex( indexId, language, collection );
		}
		catch( ConfigurationException e ) {
			e.printUsage();
		}
	}

}