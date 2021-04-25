Type=Activity
Version=7.3
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@

#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

#Extends: android.support.v7.app.AppCompatActivity

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	
	Private L_View As ListView
	Private ToolBar As ACToolBarLight
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("Layout_Data")
	ToolBar.Title="Data Mahasiswa"
	ToolBar.SubTitle ="Untuk Menampilkan Data Mahasiswa"
    CallSub(Me,ViewMahasiswa)
End Sub

Private Sub ViewMahasiswa
	Dim SearchPersons As HttpJob
	SearchPersons.Initialize("ViewMahasiswa", Me)
	SearchPersons.download2("http://" & Main.IP & "/Mahasiswa.php", Array As String ("action", "View_Mahasiswa"))
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub JobDone(Job As HttpJob)
	ProgressDialogHide
	If Job.Success Then
		Dim res As String
		res = Job.GetString
		Log("Back from Job:" & Job.JobName )
		Log("Response from server: " & res)
		Dim parser As JSONParser
		parser.Initialize(res)
		Select Job.JobName
			Case "ViewMahasiswa"
				Dim ListOfPersons As List
				Dim PersonNim,PersonNama,PersonKelas,PersonJK As String
				
				ListOfPersons = parser.NextArray 'returns a list with maps
				L_View.Clear
				If ListOfPersons.Size=0 Then
					L_View.AddSingleLine ("No persons found...")
				Else
					For i = 0 To ListOfPersons.Size - 1
						Dim Person As Map
						Person = ListOfPersons.Get(i)
						PersonNim = Person.Get("Nim")
						PersonNama = Person.Get("Nama")
						PersonKelas = Person.Get("Kelas")
						PersonJK = Person.Get("Jenis_Kelamin")
						L_View.TwoLinesLayout.Label.TextColor = Colors.Black
						L_View.AddTwoLines (PersonNim, PersonNama &", "& PersonKelas & " JK:"&PersonJK)
					Next
				End If
			Case "TambahData"
				ProgressDialogHide
			Case "CariMahasiswa"
				Dim ListOfPersons As List
				Dim PersonNim As String
				Dim PersonNama As String
				Dim PersonKelas As String
				ListOfPersons = parser.NextArray 'returns a list with maps
				If ListOfPersons.Size=0 Then
					ToastMessageShow("Data tidak ditemukan",False)
				Else
					For i = 0 To ListOfPersons.Size - 1
						Dim Person As Map
						Person = ListOfPersons.Get(i)
						PersonNim = Person.Get("Nim")
						PersonNama = Person.Get("Nama")
						PersonKelas = Person.Get("Kelas")
					Next
				End If
			Case "UbahData"
				ProgressDialogHide
			Case "HapusData"
				ProgressDialogHide
		End Select
	Else
		ToastMessageShow("Error: " & Job.ErrorMessage, True)
	End If
	Job.Release
End Sub
