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
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private ACToolBarLight1 As ACToolBarLight
	Private ACRadioButton1 As ACRadioButton
	Private ACRadioButton2 As ACRadioButton
	Private txtNim As DSFloatLabelEditText
	Private txtNama As DSFloatLabelEditText
	Private spKelas As ACSpinner
	Private rbLaki As ACRadioButton
	Private rbPr As ACRadioButton
	Private btnSimpan As ACButton
	Private btnBatal As ACButton
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	Activity.LoadLayout("Layout_Input")
	ACToolBarLight1.Title="Input Data"
	ACToolBarLight1.SubTitle="Form Untuk Input Data Mahasiswa"
	CallSub(Me,ListKelas)
End Sub

Private Sub ListKelas
	spKelas.Add("A")
	spKelas.Add("B")
	spKelas.Add("C")
	spKelas.Add("D")
End Sub

Private Sub SimpanData
	Dim Jk As Char
	If rbLaki.Checked=True Then
		Jk = "L"
		Else If rbPr.Checked=True Then
		Jk = "P" 
	End If
	Dim SearchPersons As HttpJob
	SearchPersons.Initialize("TambahData", Me)
	SearchPersons.download2("http://" & Main.IP & "/mahasiswa.php", Array As String ("action", "Insert_Mahasiswa","Nim",txtNim.Text,"Nama",txtNama.Text,"Kelas",spKelas.SelectedItem,"Jenis_Kelamin",Jk))
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
			Case "TambahData"
				ProgressDialogHide
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



Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub btnSimpan_Click
	CallSub(Me,SimpanData)
End Sub

Sub btnBatal_Click
	
End Sub