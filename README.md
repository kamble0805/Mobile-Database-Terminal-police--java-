

<p align="center">
  <img src="https://komarev.com/ghpvc/?username=ShubhamKamble&label=Profile%20Views&color=0e75b6&style=flat" alt="Profile Views" />
</p>

### **Police MDT System – Developed at Viva College, Virar, Maharashtra**  

A modern, real-time **Police Mobile Data Terminal (MDT) system** designed to enhance law enforcement efficiency. This system provides essential tools for officers to manage operations seamlessly.  

### **Key Features:**  

- **🕒 Clock-in System** – Tracks on-duty personnel and ensures accurate work logs.  
- **🚔 Active Units & Status Display** – Monitors real-time unit availability for effective dispatching.  
- **📢 Distress Calls (Real-time)** – Enables instant response to emergency situations.  
- **🔍 Name & Vehicle Lookup** – Provides quick access to citizen and vehicle records.  
- **⚖️ Warrants System** – Manages, issues, and tracks warrants efficiently.  
- **📝 Reports Section** – Allows officers to file, review, and manage incident reports.  
- **📂 Case Management & Charge Sheets** – Streamlines legal procedures for efficient case handling.  

### **Dependencies & Technologies Used:**  
- **Database:** MySQL (using **MySQL Connector/J**) for data management.  
- **Backend:** Java with JDBC for database interactions.  
- **Frontend:** Java Swing/JavaFX for UI development.  
- **Authentication:** Usernames and passwords are validated directly through the database.  
- **Version Control:** GitHub for project tracking and collaboration.  

### **Instructions to Run the Project:**  

1. **Set Up the Database:**
   - Before starting the project, you need to create all the necessary tables in the database.  
   - To do this, execute the provided **SQL file** in your MySQL database. The SQL file will create all required tables for the project.

2. **Database Setup:**
   - Since this is a prototype, the data in the database (such as names, vehicle details, etc.) are **generated using AI** and are **fake**.  
   - To ensure the system works correctly, you will need to manually add **citizens** and **vehicles** to the database.  
   - Use the provided sample SQL queries to add this data, or use the system's interface to input this data if applicable.

3. **Start the Project:**
   - Once the database is set up with the necessary tables and data, you can start the project.  
   - Run the **Java application** to launch the system. The user interface will allow officers to interact with the system, manage their duties, and generate reports.

4. **Important Notes:**
   - Make sure that the database credentials in the Java code are updated to match your local MySQL configuration.
   - As this is a prototype, future enhancements can be made, such as adding **AI-based crime pattern analysis** or **real-time GPS tracking**.

### **Use Cases & Future Refinements:**  
- Law enforcement agencies can customize and refine the system based on specific operational needs.  
- Additional features such as **AI-based crime pattern analysis**, **real-time GPS tracking**, or **automated report generation** can be integrated.  
- The system serves as a foundation for future enhancements, improving officer coordination, public safety, and legal processing.  

This MDT system is a **scalable** and **modifiable** solution, adaptable for continuous improvements. 🚀

