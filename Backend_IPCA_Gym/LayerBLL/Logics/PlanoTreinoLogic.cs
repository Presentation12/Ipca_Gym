using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using LayerDAL;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;

namespace LayerBLL.Logics
{
    public class PlanoTreinoLogic
    {
        public static async Task<Response> GetPlanoTreinosLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<PlanoTreino> planotreinoList = await PlanoTreinoService.GetPlanoTreinosService(sqlDataSource);

            if (planotreinoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult(planotreinoList);
            }

            return response;
        }
    }
}
