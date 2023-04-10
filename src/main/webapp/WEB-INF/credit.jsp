<jsp:include page="header.jsp" />

<form method="post" action="">
    <label>Credits:</label>
    <input name="credit" id="credit" type="text" />
    <br>
    <button type="submit" id="addCredit">Add credits</button>
</form>

<script>
    document.getElementById("addCredit").addEventListener("click", addCredit);

    async function addCredit() {
        let credit = document.getElementById("credit").value;
        post(
            '/credit',
            {
                'amount': credit
            }
        )
    }
</script>
<jsp:include page="footer.jsp" />
