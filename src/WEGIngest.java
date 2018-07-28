

import java.awt.Color;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/** Tool for converting formatted Excel files for the Worldwide Equipment Guide (WEG) into individual JSON data files. */
public final class WEGIngest extends javax.swing.JFrame
{
	/** File browser instance for selecting an Excel file */
	private final JFileChooser fileChooser = new JFileChooser();
	/** File browser instance for selecting an output folder */
	private final JFileChooser exportChooser = new JFileChooser();
	/** Reader instance used for validating and processing an input Excel file */
	private Reader reader;
	
	/** Initializes the GUI and file browsers */
	public WEGIngest()
	{
	    // Initialize
	    initComponents();
	 
	    // File browser
	    fileChooser.setAcceptAllFileFilterUsed( false );
	    fileChooser.setFileFilter( new FileNameExtensionFilter( "Excel (.xlsx)", "xlsx" ) );
	    
	    // Folder export browser
	    exportChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
	    exportChooser.setAcceptAllFileFilterUsed( false );
	    exportChooser.setDialogTitle( "Select Output Folder" );
	}

	@SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        openFile = new javax.swing.JButton();
        path = new javax.swing.JTextField();
        export = new javax.swing.JButton();
        message = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("WEG Asset Parser");
        setResizable(false);

        openFile.setText("Browse");
        openFile.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                openFileActionPerformed(evt);
            }
        });

        path.setEditable(false);
        path.setToolTipText("");

        export.setText("Export");
        export.setEnabled(false);
        export.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(export, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(openFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(path, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(message, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(path)
                    .addComponent(openFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(export, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(message, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        path.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

	/**
	* Callback for the {@link WEGIngest#fileChooser}'s interface that attempts to validate and prepare an input file
	* @param evt Dialog window event
	*/
    private void openFileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_openFileActionPerformed
    {//GEN-HEADEREND:event_openFileActionPerformed
		switch ( fileChooser.showOpenDialog( this ) )
		{
			case JFileChooser.APPROVE_OPTION:
				File tempFile = fileChooser.getSelectedFile();
				path.setText( tempFile.getAbsolutePath() );
				export.setEnabled( false );
				
				try
				{
					reader = new Reader( tempFile );
				}
				catch ( Exception tError )
				{
					message.setText( tError.getMessage() );
					message.setForeground( Color.red );
					
					return;
				}
				
				message.setText( "Ready for export." );
				message.setForeground( new Color( 0, 150, 0 ) );
				export.setEnabled( true );
				break;
			default:
				break;
		}
    }//GEN-LAST:event_openFileActionPerformed

	/**
	* Callback for the {@link WEGIngest#exportChooser}'s interface that attempts to fully read an input file and write output files
	* @param evt Dialog window event
	*/
    private void exportActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exportActionPerformed
    {//GEN-HEADEREND:event_exportActionPerformed
        switch ( exportChooser.showOpenDialog( this ) )
		{
			case JFileChooser.APPROVE_OPTION:
				Writer tempWriter;
				String tempPath = exportChooser.getSelectedFile().getAbsolutePath() + "\\";
				
				Asset[] tempAssets = reader.read();
				for ( int i = ( tempAssets.length - 1 ); i >= 0; --i )
				{
					tempWriter = new Writer( tempPath + tempAssets[i].getName().replaceAll( "[\\\\/:*?\"<>|]", " " ) + ".json" );
					
					try
					{
						tempWriter.write( tempAssets[i] );
					}
					catch ( Exception tError )
					{
						message.setText( tError.getMessage() );
						message.setForeground( Color.red );
						
						return;
					}
				}
				
				message.setText( "Success!" );
				message.setForeground( new Color( 0, 150, 0 ) );
				export.setEnabled( false );
				break;
			default:
				break;
		}
    }//GEN-LAST:event_exportActionPerformed
	
	/**
	* Entry point of the program to launch and display the GUI form
	* @param tArgs Entry arguments
	*/
	public static void main( String tArgs[] )
	{
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try
		{
			for ( javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels() )
			{
				if ( "Windows".equals( info.getName() ) )
				{
					javax.swing.UIManager.setLookAndFeel( info.getClassName() );
					break;
				}
			}
		}
		catch ( ClassNotFoundException ex )
		{
			java.util.logging.Logger.getLogger(WEGIngest.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		}
		catch ( InstantiationException ex )
		{
			java.util.logging.Logger.getLogger(WEGIngest.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		}
		catch ( IllegalAccessException ex )
		{
			java.util.logging.Logger.getLogger(WEGIngest.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		}
		catch ( javax.swing.UnsupportedLookAndFeelException ex )
		{
			java.util.logging.Logger.getLogger(WEGIngest.class.getName() ).log( java.util.logging.Level.SEVERE, null, ex );
		}
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater
		(new Runnable()
			{
				public void run()
				{
					new WEGIngest().setVisible( true );
				}
			}
		);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton export;
    private javax.swing.JLabel message;
    private javax.swing.JButton openFile;
    private javax.swing.JTextField path;
    // End of variables declaration//GEN-END:variables
}