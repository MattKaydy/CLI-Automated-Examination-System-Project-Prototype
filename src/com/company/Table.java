/*
In-Memory Virtual File System

*/
package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 *  A class which would create a table.
 * It supports five titles at most and with no limitation on the rows.
 * It has been used in printing out printAllCriteria, search and rsearch
 *
 */

public class Table {
    private  List<String> title = new ArrayList<String>();
    private List<String> rows = new ArrayList<String>();
    private List [] columns;

    private final int[] titleLength;                        // Length of each title. E.g. titleLength[0] is the length of title 1
    private int numberOfAttribute = 0;
    private int totalLength = 0;                            // Total length in each record.
    public int numberOfCombine = 0;                         // How many times that .combine has been used

    /**
     * Constructor: Generate one title.
     *
     * @param titleOne - Title for first column
     * @param titleOneLength - Length of first column. E.g. "abc" is in length 3
     */
    public Table(String titleOne, int titleOneLength) {
        int numberOfTitle = 1;
        // The length of titleOne must < titleOneLength
        if(titleOne.length() > titleOneLength)
            throw new IllegalArgumentException();
        // initialise columns
        columns = new List[numberOfTitle];
        for(int i = 0; i < numberOfTitle;i++) {
            columns[i] = new ArrayList<String>();
        }
        // initialise title
        title.add(titleOne);

        this.titleLength = new int[numberOfTitle];
        this.titleLength[0] = titleOneLength;
        this.numberOfAttribute = titleLength.length;
        this.totalLength = titleOneLength;
    }

    /**
     * Constructor: Generate two titles
     *
     * @param titleOne - Title for first column
     * @param titleOneLength - Length of first column. E.g. "abc" is in length 3
     * @param titleTwo - Title for second column.
     * @param titleTwoLength - Length of second column.
     */
    public Table(String titleOne, int titleOneLength, String titleTwo, int titleTwoLength) {
        int numberOfTitle = 2;
        // The length of title must < titleLength
        if(titleOne.length() > titleOneLength || titleTwo.length() > titleTwoLength)
            throw new IllegalArgumentException();
        // initialise columns
        columns = new List[numberOfTitle];
        for(int i = 0; i < numberOfTitle;i++) {
            columns[i] = new ArrayList<String>();
        }

        // initialise title
        title.add(titleOne);
        title.add(titleTwo);

        this.titleLength = new int[numberOfTitle];
        this.titleLength[0] = titleOneLength;
        this.titleLength[1] = titleTwoLength;
        this.numberOfAttribute = titleLength.length;
        this.totalLength = titleOneLength + titleTwoLength;
    }


    /**
     * Constructor: Generate three titles.
     *
     * @param titleOne - Title for first column
     * @param titleOneLength - Length of first column. E.g. "abc" is in length 3
     * @param titleTwo - Title for second column.
     * @param titleTwoLength - Length of second column.
     * @param titleThree - Title for third column.
     * @param titleThreeLength - Length of third column.
     */
    public Table(String titleOne, int titleOneLength, String titleTwo, int titleTwoLength, String titleThree, int titleThreeLength) {
        int numberOfTitle = 3;
        // The length of title must < titleLength
        if(titleOne.length() > titleOneLength || titleTwo.length() > titleTwoLength || titleThree.length() > titleThreeLength)
            throw new IllegalArgumentException();
        // initialise columns
        columns = new List[numberOfTitle];
        for(int i = 0; i < numberOfTitle;i++) {
            columns[i] = new ArrayList<String>();
        }

        // initialise title
        title.add(titleOne);
        title.add(titleTwo);
        title.add(titleThree);

        this.titleLength = new int[numberOfTitle];
        this.titleLength[0] = titleOneLength;
        this.titleLength[1] = titleTwoLength;
        this.titleLength[2] = titleThreeLength;
        this.numberOfAttribute = titleLength.length;
        this.totalLength = titleOneLength + titleTwoLength + titleThreeLength;
    }


    /**
     * Constructor: Generate four titles.
     *
     * @param titleOne - Title for first column
     * @param titleOneLength - Length of first column. E.g. "abc" is in length 3
     * @param titleTwo - Title for second column.
     * @param titleTwoLength - Length of second column.
     * @param titleThree - Title for third column.
     * @param titleThreeLength - Length of third column.
     * @param titleFour -  Title for fourth column.
     * @param titleFourLength - Length of fourth column.
     */
    public Table(String titleOne, int titleOneLength, String titleTwo, int titleTwoLength, String titleThree, int titleThreeLength, String titleFour, int titleFourLength) {
        int numberOfTitle = 4;
        // The length of title must < titleLength
        if(titleOne.length() > titleOneLength || titleTwo.length() > titleTwoLength || titleThree.length() > titleThreeLength || titleFour.length() > titleFourLength)
            throw new IllegalArgumentException();
        // initialise columns
        columns = new List[numberOfTitle];
        for(int i = 0; i < numberOfTitle;i++) {
            columns[i] = new ArrayList<String>();
        }

        // initialise title
        title.add(titleOne);
        title.add(titleTwo);
        title.add(titleThree);
        title.add(titleFour);

        this.titleLength = new int[numberOfTitle];
        this.titleLength[0] = titleOneLength;
        this.titleLength[1] = titleTwoLength;
        this.titleLength[2] = titleThreeLength;
        this.titleLength[3] = titleFourLength;
        this.numberOfAttribute = titleLength.length;
        this.totalLength = titleOneLength + titleTwoLength + titleThreeLength + titleFourLength;
    }

    /**
     * Constructor: Generate five titles.
     *
     * @param titleOne - Title for first column
     * @param titleOneLength - Length of first column. E.g. "abc" is in length 3
     * @param titleTwo - Title for second column.
     * @param titleTwoLength - Length of second column.
     * @param titleThree - Title for third column.
     * @param titleThreeLength - Length of third column.
     * @param titleFour -  Title for fourth column.
     * @param titleFourLength - Length of fourth column.
     * @param titleFive - Title for fifth column.
     * @param titleFiveLength - Length of fifth column.
     */
    public Table(String titleOne, int titleOneLength, String titleTwo, int titleTwoLength, String titleThree, int titleThreeLength, String titleFour, int titleFourLength, String titleFive, int titleFiveLength) {
        int numberOfTitle = 5;
        // The length of title must < titleLength
        if(titleOne.length() > titleOneLength || titleTwo.length() > titleTwoLength || titleThree.length() > titleThreeLength || titleFour.length() > titleFourLength || titleFive.length() > titleFiveLength)
            throw new IllegalArgumentException();
        // initialise columns
        columns = new List[numberOfTitle];
        for(int i = 0; i < numberOfTitle;i++) {
            columns[i] = new ArrayList<String>();
        }

        // initialise title
        title.add(titleOne);
        title.add(titleTwo);
        title.add(titleThree);
        title.add(titleFour);
        title.add(titleFive);

        this.titleLength = new int[numberOfTitle];
        this.titleLength[0] = titleOneLength;
        this.titleLength[1] = titleTwoLength;
        this.titleLength[2] = titleThreeLength;
        this.titleLength[3] = titleFourLength;
        this.titleLength[4] = titleFiveLength;
        this.numberOfAttribute = titleLength.length;
        this.totalLength = titleOneLength + titleTwoLength + titleThreeLength + titleFourLength + titleFiveLength;
    }

    /**
     *
     * @return - return title in type List<String></>
     */
    public List<String> getTitle() {
        return title;
    }

    /**
     *
     * @param index - index for the row. The index of first row is 0.
     * @return - the row in String.
     */
    public String getARow(int index) {
        return rows.get(index);
    }

    /**
     *
     * @return - return all Rows in type List<String></>
     */
    public List<String> getRows() {
        return rows;
    }

    /**
     *
     * @return - return the number of rows in int.
     */
    public int getNumberOfRows() {
        return rows.size();
    }

    /**
     *
     * @return - return the Attributes of rows in int.
     */
    public int getNumberOfAttribute() {
        return numberOfAttribute;
    }

    // Get particular column

    /**
     *
     * @param index - index for the column. The index of first column is 0.
     * @return - the whole column in List.
     */
    public List getAColumn(int index) {
        return columns[index];
    }

    /**
     * Get all columns.
     *
     * @return - Return all columns.
     */
    public List[] getColumns() {
        return columns;
    }

    /**
     *
     * @return - Total length in each record.
     */
    public int getTotalLength() {
        return totalLength;
    }

    /**
     * Add row for one title.
     * Only String is allowed in attributes.
     *
     * @param attributeOne - First attribute.
     */
    public void add(String attributeOne) {
        int numberOfAttribute = 1;

        // Turn null into "null"
        if(attributeOne == null) {
            attributeOne = "null";
        }
        if (attributeOne.length() > this.titleLength[numberOfAttribute - 1])
            throw new IllegalArgumentException();
        // Adding attribute into rows
        String eachRecord = "";
        rows.add(eachRecord.concat(attributeOne));
        // Adding attribute into columns
        columns[numberOfAttribute - 1].add(attributeOne);
    }

    /**
     * Add row for two title.
     * Only String is allowed in attributes.
     *
     * @param attributeOne - First attribute.
     * @param attributeTwo - Second attribute.
     */
    public void add(String attributeOne, String attributeTwo) {
        // Throw IllegalStateException if the number of attribute is wrong
        if(numberOfAttribute != 2)
            throw new IllegalStateException();

        // Put all attribute into the array
        String[] attributes = new String[numberOfAttribute];
        attributes[0] = attributeOne;
        attributes[1] = attributeTwo;

        // Throw IllegalArgumentException if length of attribute > the title length
        for(int i = 0; i < numberOfAttribute; i++) {
            // Turn null into "null"
            if(attributes[i] == null) {
                attributes[i] = "null";
            }
            if (attributes[i].length() > this.titleLength[i])
                throw new IllegalArgumentException();
        }
        // Put into rows
        addAllAttributesIntoRows(attributes);

        // Put into columns;
        addAllAttributesIntoColumns(attributes);
    }

    /**
     * Add row for three title.
     * Only String is allowed in attributes.
     *
     * @param attributeOne - First attribute.
     * @param attributeTwo - Second attribute.
     * @param attributeThree - Third attribute.
     */
    public void add(String attributeOne, String attributeTwo, String attributeThree) {
        // Throw IllegalStateException if the number of attribute is wrong
        if(numberOfAttribute != 3)
            throw new IllegalStateException();

        // Put all attribute into the array
        String[] attributes = new String[numberOfAttribute];
        attributes[0] = attributeOne;
        attributes[1] = attributeTwo;
        attributes[2] = attributeThree;

        // Throw IllegalArgumentException if length of attribute > the title length
        for(int i = 0; i < numberOfAttribute; i++) {
            // Turn null into "null"
            if(attributes[i] == null) {
                attributes[i] = "null";
            }
            if (attributes[i].length() > this.titleLength[i])
                throw new IllegalArgumentException();
        }
        // Put into rows:
        addAllAttributesIntoRows(attributes);

        // Put into columns:
        addAllAttributesIntoColumns(attributes);
    }

    /**
     * Add row for four title.
     * Only String is allowed in attributes.
     *
     * @param attributeOne - First attribute.
     * @param attributeTwo - Second attribute.
     * @param attributeThree - Third attribute.
     * @param attributeFour - Fourth attribute.
     */
    public void add(String attributeOne, String attributeTwo, String attributeThree, String attributeFour) {
        // Throw IllegalStateException if the number of attribute is wrong
        if(numberOfAttribute != 4)
            throw new IllegalStateException();

        // Put all attribute into the array
        String[] attributes = new String[numberOfAttribute];
        attributes[0] = attributeOne;
        attributes[1] = attributeTwo;
        attributes[2] = attributeThree;
        attributes[3] = attributeFour;

        // Throw IllegalArgumentException if length of attribute > the title length
        for(int i = 0; i < numberOfAttribute; i++) {
            // Turn null into "null"
            if(attributes[i] == null) {
                attributes[i] = "null";
            }
            if (attributes[i].length() > this.titleLength[i])
                throw new IllegalArgumentException();
        }

        // Put into rows
        addAllAttributesIntoRows(attributes);

        // Put into columns:
        addAllAttributesIntoColumns(attributes);
    }

    /**
     * Add row for five title.
     * Only String is allowed in attributes.
     *
     * @param attributeOne - First attribute.
     * @param attributeTwo - Second attribute.
     * @param attributeThree - Third attribute.
     * @param attributeFour - Fourth attribute.
     * @param attributeFive - Fifth attribute.
     */
    public void add(String attributeOne, String attributeTwo, String attributeThree, String attributeFour, String attributeFive) {
        // Throw IllegalStateException if the number of attribute is wrong
        if(numberOfAttribute != 5)
            throw new IllegalStateException();

        // Put all attribute into the array
        String[] attributes = new String[numberOfAttribute];
        attributes[0] = attributeOne;
        attributes[1] = attributeTwo;
        attributes[2] = attributeThree;
        attributes[3] = attributeFour;
        attributes[4] = attributeFive;

        // Throw IllegalArgumentException if length of attribute > the title length
        for(int i = 0; i < numberOfAttribute; i++) {
            // Turn null into "null"
            if(attributes[i] == null) {
                attributes[i] = "null";
            }
            if (attributes[i].length() > this.titleLength[i])
                throw new IllegalArgumentException();
        }

        // Put into rows:
        addAllAttributesIntoRows(attributes);

        // Put into columns:
        addAllAttributesIntoColumns(attributes);
    }

    /**
     * Private used by add()
     * Add All Attributes Into Rows.
     *
     * @param attributes - All attributes in String[].
     */
    private void addAllAttributesIntoRows(String[] attributes) {
        // add each record into rows
        String eachRecord = "";
        int numberOfSpacesRemains = 0;
        for(int i = 0; i < numberOfAttribute; i++) {
            eachRecord = eachRecord.concat(attributes[i]);

            // Spacing
            numberOfSpacesRemains = titleLength[i] - attributes[i].length();
            String space = "";
            for(int m = 0; m < numberOfSpacesRemains; m++) {
                space = space.concat(" ");
            }
            // Add spacing into row
            eachRecord = eachRecord.concat(space);
            numberOfSpacesRemains = 0;
        }
        // Finally add into rows
        rows.add(eachRecord);
    }

    /**
     * Private used by add()
     * add All Attributes Into Columns
     *
     * @param attributes - All attributes in String[].
     */
    private void addAllAttributesIntoColumns(String[] attributes) {
        int numberOfAttributes = attributes.length;

        for(int i = 0; i < numberOfAttributes; i++) {
            columns[i].add(attributes[i]);
        }
    }

    /**
     * Add the entire row at one time. All spacing would be the work for the user if user uses this method.
     * use spaces to identify all attributes.
     *
     * @param row - A string of one row.
     */
    public void addOneRow(String row) {
        this.rows.add(row);

        String trimmedStr = row.replaceAll("\\s+"," ");
        String[] attributes = trimmedStr.split(" ");

        addAllAttributesIntoColumns(attributes);
    }

    /**
     * Combine newTable with this.
     *
     * @param newTable - new table which user would want to combine with this.
     */
    public void combine(Table newTable) {
        // Number of attribute of two tables must be equal.
        if(this.getNumberOfAttribute() != newTable.getNumberOfAttribute())
            throw new IllegalArgumentException();
        // Update numberOfCombine
        numberOfCombine += 1;


        // Combing
        for(int i = 0; i < newTable.getNumberOfRows(); i++) {
            this.addOneRow(newTable.getARow(i));
        }
    }

    /**
     *  Printing method.
     *  print out the title only.
     */
    public void printTitle() {
        int numberOfSpacesRemains = 0;
        for (int i = 0; i < title.size(); i++) {
            System.out.print(title.get(i));

            // Spacing
            numberOfSpacesRemains = titleLength[i] - title.get(i).length();
            String space = "";
            for(int m = 0; m < numberOfSpacesRemains; m++) {
                space = space.concat(" ");
            }

            // Print out spacing
            System.out.print(space);
        }
        System.out.println("");

        // Print out the line between title and records
        String line = "";
        int indexForAdjustingLineLength = 1;
        for(int i = 0; i < totalLength * indexForAdjustingLineLength; i++) {
            line = line.concat("=");

            // Maximum length of '=': 185
            if(i == 185)
                break;
        }
        System.out.println(line);
    }

    /**
     * Printing method.
     * print out rows only.
     */
    public void printRows() {
        for (int i = 0; i < rows.size(); i++) {
            System.out.println(rows.get(i));
        }
    }

    /**
     * Printing method.
     * Print out the entire table.
     */
    public void printAll() {
        // Printing title
        printTitle();
        // Print out all records
        printRows();
    }

    // Static method -------------------------------------------------------------------------------------------------

    // Combine two tables.

    /**
     * Combine table A and Table B.
     *
     * @param oldTable - Table A
     * @param newTable - Table B
     * @return - Combined table.
     */
    public static Table combineTwoTable(Table oldTable, Table newTable) {
        // Number of attribute of two tables must be equal.
        if(oldTable.getNumberOfAttribute() != newTable.getNumberOfAttribute())
            throw new IllegalArgumentException();

        // Combing
        for(int i = 0; i < newTable.getNumberOfRows(); i++) {
            oldTable.addOneRow(newTable.getARow(i));
        }
        return oldTable;
    }

}



