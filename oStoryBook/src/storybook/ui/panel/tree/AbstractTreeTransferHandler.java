package storybook.ui.panel.tree;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public abstract class AbstractTreeTransferHandler
    implements DragGestureListener, DragSourceListener, DropTargetListener {

     private Tree tree;
     private TreePanel treePanel;
     private DragSource dragSource; // dragsource
     private static TreeNode draggedNode; 
     private TreeNode draggedNodeParent; 
     private static BufferedImage image = null; //buff image
     private Rectangle rect2D = new Rectangle();
     private boolean drawImage;

     protected AbstractTreeTransferHandler(TreePanel treePanel, int action, boolean drawIcon) {
         this.treePanel = treePanel;
          this.tree = treePanel.getTree();
          drawImage = drawIcon;
          dragSource = new DragSource();
          dragSource.createDefaultDragGestureRecognizer(tree, action, this);
          new DropTarget(tree, action, this);
     }

     /* Methods for DragSourceListener */
     public void dragDropEnd(DragSourceDropEvent dsde) {
          if (dsde.getDropSuccess() && dsde.getDropAction()==DnDConstants.ACTION_MOVE && draggedNodeParent != null) {
               ((DefaultTreeModel)tree.getModel()).nodeStructureChanged(draggedNodeParent);                    
          }
     }
     public final void dragEnter(DragSourceDragEvent dsde)  {
          int action = dsde.getDropAction();
          if (action == DnDConstants.ACTION_COPY)  {
               dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
          } 
          else {
               if (action == DnDConstants.ACTION_MOVE) {
                    dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
               } 
               else {
                    dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
               }
          }
     }
     public final void dragOver(DragSourceDragEvent dsde) {
          int action = dsde.getDropAction();
          if (action == DnDConstants.ACTION_COPY) {
               dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
          } 
          else  {
               if (action == DnDConstants.ACTION_MOVE) {
                    dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
               } 
               else  {
                    dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
               }
          }
     }
     public final void dropActionChanged(DragSourceDragEvent dsde)  {
          int action = dsde.getDropAction();
          if (action == DnDConstants.ACTION_COPY) {
               dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
          }
          else  {
               if (action == DnDConstants.ACTION_MOVE) {
                    dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
               } 
               else {
                    dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
               }
          }
     }
     public final void dragExit(DragSourceEvent dse) {
        dse.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
     }     
          
     /* Methods for DragGestureListener */
     public final void dragGestureRecognized(DragGestureEvent dge) {
          TreePath path = tree.getSelectionPath(); 
          if (path != null) { 
               draggedNode = (TreeNode)path.getLastPathComponent();
               draggedNodeParent = (TreeNode)draggedNode.getParent();
               if (drawImage) {
                    Rectangle pathBounds = tree.getPathBounds(path); //getpathbounds of selectionpath
                    JComponent lbl = (JComponent)tree.getCellRenderer().getTreeCellRendererComponent(tree, draggedNode, false , tree.isExpanded(path),((DefaultTreeModel)tree.getModel()).isLeaf(path.getLastPathComponent()), 0,false);//returning the label
                    lbl.setBounds(pathBounds);//setting bounds to lbl
                    image = new BufferedImage(lbl.getWidth(), lbl.getHeight(), java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE);//buffered image reference passing the label's ht and width
                    Graphics2D graphics = image.createGraphics();//creating the graphics for buffered image
                    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));     //Sets the Composite for the Graphics2D context
                    lbl.setOpaque(false);
                    lbl.paint(graphics); //painting the graphics to label
                    graphics.dispose();                    
               }
               dragSource.startDrag(dge, DragSource.DefaultMoveNoDrop , image, new Point(0,0), new TransferableNode(draggedNode), this);               
          }      
     }

     /* Methods for DropTargetListener */

     public final void dragEnter(DropTargetDragEvent dtde) {
          Point pt = dtde.getLocation();
          int action = dtde.getDropAction();
          if (drawImage) {
               paintImage(pt);
          }
          if (canPerformAction(tree, draggedNode, action, pt)) {
               dtde.acceptDrag(action);               
          }
          else {
               dtde.rejectDrag();
          }
     }

     public final void dragExit(DropTargetEvent dte) {
          if (drawImage) {
               clearImage();
          }
     }

     public final void dragOver(DropTargetDragEvent dtde) {
          Point pt = dtde.getLocation();
          int action = dtde.getDropAction();
          tree.autoscroll(pt);
          if (drawImage) {
               paintImage(pt);
          }
          if (canPerformAction(tree, draggedNode, action, pt)) {
               dtde.acceptDrag(action);               
          }
          else {
               dtde.rejectDrag();
          }
     }

     public final void dropActionChanged(DropTargetDragEvent dtde) {
          Point pt = dtde.getLocation();
          int action = dtde.getDropAction();
          if (drawImage) {
               paintImage(pt);
          }
          if (canPerformAction(tree, draggedNode, action, pt)) {
               dtde.acceptDrag(action);               
          }
          else {
               dtde.rejectDrag();
          }
     }

     public final void drop(DropTargetDropEvent dtde) {
          try {
               if (drawImage) {
                    clearImage();
               }
               int action = dtde.getDropAction();
               Transferable transferable = dtde.getTransferable();
               Point pt = dtde.getLocation();
               if (transferable.isDataFlavorSupported(TransferableNode.NODE_FLAVOR) && canPerformAction(tree, draggedNode, action, pt)) {
                    TreePath pathTarget = tree.getPathForLocation(pt.x, pt.y);
                    TreeNode node = (TreeNode) transferable.getTransferData(TransferableNode.NODE_FLAVOR);
                    TreeNode newParentNode =(TreeNode)pathTarget.getLastPathComponent();
                    if (executeDrop(tree, node, newParentNode, action)) {
                         dtde.acceptDrop(action);                    
                         dtde.dropComplete(true);
                         return;                         
                    }
               }
               dtde.rejectDrop();
               dtde.dropComplete(false);
          }          
          catch (Exception e) {     
               dtde.rejectDrop();
               dtde.dropComplete(false);
          }     
     }
     
     private final void paintImage(Point pt) {
//        System.out.println(pt);
//          tree.paintImmediately(rect2D.getBounds());
//          rect2D.setRect((int) pt.getX(),(int) pt.getY(),image.getWidth(),image.getHeight());
//          tree.getGraphics().drawImage(image,(int) pt.getX(),(int) pt.getY(),tree);
     }

     private final void clearImage() {
          tree.paintImmediately(rect2D.getBounds());
     }
     
     public Tree getTree() {
    	 return tree;
     }
     
     protected TreePanel getTreePanel() {
    	 return treePanel;
     }

     public abstract boolean canPerformAction(Tree target, TreeNode draggedNode, int action, Point location);

     public abstract boolean executeDrop(Tree tree, TreeNode draggedNode, TreeNode newParentNode, int action);
}
