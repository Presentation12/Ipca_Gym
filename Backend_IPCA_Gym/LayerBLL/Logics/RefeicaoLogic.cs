using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using LayerDAL.Services;

namespace LayerBLL.Logics
{
    public class RefeicaoLogic
    {
        public static async Task<Response> GetRefeicoesLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Refeicao> refeicaoList = await RefeicaoService.GetRefeicoesService(sqlDataSource);

            if (refeicaoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult(refeicaoList);
            }

            return response;
        }
    }
}