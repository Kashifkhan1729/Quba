Quba - Student Marksheet Generator

Quba is an Android application designed for schools to generate professional student marksheets in PDF format. The app simplifies the process of creating detailed academic reports with subject-wise marks, totals, percentages, and divisions.

Features

· 📝 Student Details Management: Capture student name, father's name, roll number, and class information
· 🎯 Class-wise Subjects: Automatically loads appropriate subjects based on selected class (Nursery to 5th)
· 📊 Marks Entry: Input half-yearly and annual marks for each subject with validation (0-50 range)
· 📅 Date Selection: Easy date picker for selecting the report date
· 📄 PDF Generation: Creates professional PDF marksheets with:
  · School header and recognition details
  · Student information section
  · Subject-wise marks table with totals
  · Percentage calculation and division determination
  · Teacher and principal signature fields
· 💾 Automatic File Management: Saves PDFs in organized class-wise folders in Documents directory
· 🎨 Modern UI: Clean, intuitive Material Design 3 interface with responsive layout

Supported Classes

· Nursery
· KG (Kindergarten)
· 1st Grade
· 2nd Grade
· 3rd Grade
· 4th Grade
· 5th Grade

Installation

1. Clone the repository:

```bash
git clone https://github.com/KashifKhan1729/Quba.git
```

1. Open the project in Android Studio
2. Build and run the application on your Android device or emulator

Usage

1. Launch the Quba application
2. Fill in the student details:
   · Student Name
   · Father's Name
   · Select Class from dropdown
   · Roll Number
   · Date (click to select from calendar)
3. Enter marks for each subject:
   · Half Yearly marks (0-50)
   · Annual marks (0-50)
4. Click "Generate PDF" to create and save the marksheet
5. Use "Clear Form" to reset all fields

PDF Output

Generated PDFs are saved in: Documents/Quba/[Class Name]/[Student Name].pdf

The PDF includes:

· School information and recognition details
· Complete student information
· Subject-wise marks with half-yearly and annual columns
· Grand totals and calculated percentage
· Division determination (First, Second, Third, or Fail)
· Date and signature fields

Technology Stack

· Kotlin: Primary programming language
· Jetpack Compose: Modern declarative UI toolkit
· Material Design 3: Latest Material Design components
· iTextPDF: PDF generation and manipulation library
· AndroidX Libraries: Core Android components

Requirements

· Android 8.0 (API level 26) or higher
· Storage permissions for saving PDF files

Contributing

We welcome contributions to improve Quba! Please feel free to:

1. Fork the repository
2. Create a feature branch (git checkout -b feature/amazing-feature)
3. Commit your changes (git commit -m 'Add some amazing feature')
4. Push to the branch (git push origin feature/amazing-feature)
5. Open a Pull Request

License

This project is licensed under the MIT License - see the LICENSE file for details.

Support

If you encounter any issues or have questions about using Quba, please:

1. Check the existing Issues
2. Create a new issue with detailed description of the problem
3. Include device information and steps to reproduce if applicable

Acknowledgments

· iTextPDF team for the excellent PDF library
· Android Jetpack team for Compose framework
· Material Design team for the design system

---

Note: This application is designed for educational purposes and may require customization for specific institutional requirements.
