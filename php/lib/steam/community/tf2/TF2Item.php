<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */

require_once STEAM_CONDENSER_PATH . 'steam/community/WebApi.php';

/**
 * Represents a Team Fortress 2 item
 *
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class TF2Item {

    /**
     * @var array
     */
    private static $CLASSES = array('scout', 'sniper', 'soldier', 'demoman',
                                    'medic', 'heavy', 'pyro', 'spy');

    /**
     * @var array
     */
    private static $attributeSchema = null;

    /**
     * @var array
     */
    private static $itemSchema = null;

    /**
     * @var array
     */
    private static $qualities;

    /**
     * @var String
     */
    private static $schemaLanguage = 'en';

    /**
     * @var array
     */
    private $attributes;

    /**
     * @var int
     */
    private $backpackPosition;

    /**
     * @var String
     */
    private $class;

    /**
     * @var int
     */
    private $count;

    /**
     * @var int
     */
    private $defindex;

    /**
     * @var array
     */
    private $equipped;

    /**
     * @var int
     */
    private $id;

    /**
     * @var int
     */
    private $level;

    /**
     * @var String
     */
    private $name;

    /**
     * @var int
     */
    private $quality;

    /**
     * @var String
     */
    private $slot;

    /**
     * @var String
     */
    private $type;

    /**
     * Returns the attribute schema
     *
     * The attribute schema is fetched first if not done already
     */
    public static function getAttributeSchema() {
        if(self::$attributeSchema == null) {
            self::updateSchema();
        }

        return self::$attributeSchema;
    }

    /**
     * Returns the item schema
     *
     * The item schema is fetched first if not done already
     */
    public static function getItemSchema() {
        if(self::$itemSchema == null) {
            self::updateSchema();
        }

        return self::$itemSchema;
    }

    /**
     * Sets the language the schema should be fetched in (default is: +'en'+)
     */
    public static function setSchemaLanguage($language) {
        self::$schemaLanguage = $language;
    }

  /**
   * Creates a new instance of a TF2Item with the given data
   */
    public function __construct($itemData) {
        if(self::$itemSchema == null) {
            self::updateSchema();
        }

        $this->defindex = $itemData->defindex;

        $this->backpackPosition = $itemData->inventory & 0xffff;
        $this->class            = self::$itemSchema[$this->defindex]->item_class;
        $this->count            = $itemData->quantity;
        $this->id               = $itemData->id;
        $this->level            = $itemData->level;
        $this->name             = self::$itemSchema[$this->defindex]->item_name;
        $this->quality          = self::$qualities[$itemData->quality];
        $this->slot             = self::$itemSchema[$this->defindex]->item_slot;
        $this->type             = self::$itemSchema[$this->defindex]->item_type_name;

        $this->equipped = array();
        foreach(self::$CLASSES as $classId => $className) {
            $this->equipped[$className] = ($itemData->inventory & (1 << 16 + $classId) != 0);
        }

        if(self::$itemSchema[$this->defindex]->attributes != null) {
          $this->attributes = self::$itemSchema[$this->defindex]->attributes->attribute;
        }
    }

    public function getAttributes() {
        return $this->attributes;
    }

    public function getBackpackPosition() {
        return $this->backpackPosition;
    }

    public function getClass() {
        return $this->class;
    }

    public function getCount() {
        return $this->count;
    }

    public function getDefIndex() {
        return $this->defindex;
    }

    public function getId() {
        return $this->id;
    }

    public function getLevel() {
        return $this->level;
    }

    public function getName() {
        return $this->name;
    }

    public function getQuality() {
        return $this->quality;
    }

    public function getSlot() {
        return $this->slot;
    }

    public function getType() {
        return $this->type;
    }

    /**
     * Returns the class symbols for each class this player has equipped this item
     */
    public function getClassesEquipped() {
        $classesEquipped = array();
        foreach($this->equipped as $classId => $equipped) {
            if($equipped) {
                $classesEquipped[] = $classId;
            }
        }

        return $classesEquipped;
    }

    /**
     * Returns whether this item is equipped by this player at all
     */
    public function isEquipped() {
        return in_array(true, $this->equipped);
    }

    /**
     * Updates the item schema (this includes attributes and qualities) using
     * the "GetSchema" method of interface "ITFItems_440"
     */
    protected function updateSchema() {
        $params = array();
        if(self::$schemaLanguage != null) {
            $params['language'] = self::$schemaLanguage;
        }
        $result = WebApi::getJSONData('ITFItems_440', 'GetSchema', 1, $params);

        self::$attributeSchema = array();
        foreach($result->attributes->attribute as $attributeData) {
          self::$attributeSchema[$attributeData->name] = $attributeData;
        }

        self::$itemSchema = array();
        foreach($result->items->item as $itemData) {
          self::$itemSchema[$itemData->defindex] = $itemData;
        }

        self::$qualities = array();
        foreach($result->qualities as $quality => $id) {
          self::$qualities[$id] = $quality;
        }
    }

}
?>
