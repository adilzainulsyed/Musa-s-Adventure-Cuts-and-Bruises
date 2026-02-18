package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler keyH;
    
    public final int screenX;
    public final int screenY;
    
    int spriteCounter = 0;
    int spriteNum = 1;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        screenX = gp.screenWidth / 2 - gp.tileSize / 2;
        screenY = gp.screenHeight / 2 - gp.tileSize / 2;
        
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        
        setDefaultValues();
        loadPlayerImages();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "up";
    }

    public void update() {
        boolean moving = false;
        
        if (keyH.upPressed) {
            direction = "up";
            worldY -= speed;
            moving = true;
        } else if (keyH.downPressed) {
            direction = "down";
            worldY += speed;
            moving = true;
        } else if (keyH.leftPressed) {
            direction = "left";
            worldX -= speed;
            moving = true;
        } else if (keyH.rightPressed) {
            direction = "right";
            worldX += speed;
            moving = true;
        }
        
        // Check collision
        collisionOn = false;
        gp.cChecker.checkTile(this);
        
        if (collisionOn) {
            switch (direction) {
                case "up":
                    worldY += speed;
                    break;
                case "down":
                    worldY -= speed;
                    break;
                case "left":
                    worldX += speed;
                    break;
                case "right":
                    worldX -= speed;
                    break;
            }
        }
        
        if (moving) {
            spriteCounter++;
            if (spriteCounter > 10) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        
        switch (direction) {
            case "up":
                image = (spriteNum == 1) ? up1 : up2;
                break;
            case "down":
                image = (spriteNum == 1) ? down1 : down2;
                break;
            case "left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
        }
        
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    private BufferedImage flipHorizontally(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flipped = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = flipped.createGraphics();
        g2.drawImage(image, width, 0, -width, height, null);
        g2.dispose();
        return flipped;
    }

    public void loadPlayerImages() {
        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResource("/player/chibi-layered.png"));
            
            int frameWidth = 16;
            int frameHeight = 16;
            
            down1 = spriteSheet.getSubimage(0 * frameWidth, 0, frameWidth, frameHeight);
            left1 = spriteSheet.getSubimage(1 * frameWidth, 0, frameWidth, frameHeight);
            up1 = spriteSheet.getSubimage(2 * frameWidth, 0, frameWidth, frameHeight);
            down2 = spriteSheet.getSubimage(3 * frameWidth, 0, frameWidth, frameHeight);
            left2 = spriteSheet.getSubimage(4 * frameWidth, 0, frameWidth, frameHeight);
            up2 = spriteSheet.getSubimage(5 * frameWidth, 0, frameWidth, frameHeight);
            down3 = spriteSheet.getSubimage(6 * frameWidth, 0, frameWidth, frameHeight);
            left3 = spriteSheet.getSubimage(7 * frameWidth, 0, frameWidth, frameHeight);
            up3 = spriteSheet.getSubimage(8 * frameWidth, 0, frameWidth, frameHeight);
            
            right1 = flipHorizontally(left1);
            right2 = flipHorizontally(left2);
            right3 = flipHorizontally(left3);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
